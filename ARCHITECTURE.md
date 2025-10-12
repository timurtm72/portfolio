# Архитектура безопасности портфолио

## 🔒 Схема защиты с Caddy

```
┌─────────────────────────────────────────────────────────────────┐
│                         ИНТЕРНЕТ                                │
│                    (любой пользователь)                         │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
                ┌──────────────────────┐
                │  Порт 80 (HTTP)      │  ← Открыт
                │  Порт 443 (HTTPS)    │  ← Открыт
                └──────────┬───────────┘
                           │
                           ▼
        ┌──────────────────────────────────────────┐
        │         Caddy Reverse Proxy              │
        │  ✅ Автоматический SSL (Let's Encrypt)  │
        │  ✅ Rate Limiting (100 req/min)          │
        │  ✅ Security Headers                     │
        │  ✅ Блокировка ботов                     │
        │  ✅ Логирование атак                     │
        └──────────────────┬───────────────────────┘
                           │
                           │ reverse_proxy localhost:3000
                           │
                           ▼
        ┌──────────────────────────────────────────┐
        │    Frontend (Nginx в контейнере)         │
        │    localhost:3000 (127.0.0.1:3000:80)    │
        │    ⚠️ Доступен ТОЛЬКО для Caddy          │
        └──────────────────┬───────────────────────┘
                           │
                           │ proxy_pass /api/* → backend:8080
                           │
                           ▼
        ┌──────────────────────────────────────────┐
        │    Backend (Spring Boot)                 │
        │    Порт 8080 (внутренний Docker)         │
        │    🔒 ЗАКРЫТ от интернета                │
        └──────────────────┬───────────────────────┘
                           │
                           ▼
        ┌──────────────────────────────────────────┐
        │    PostgreSQL                            │
        │    Порт 5432 (внутренний Docker)         │
        │    🔒 ЗАКРЫТ от интернета                │
        └──────────────────────────────────────────┘
```

---

## 🌐 Поток HTTP запроса

### Пример: Пользователь открывает сайт

```
1. Браузер пользователя
   └─> https://portfolio.timurtm72.ru
       │
       ▼
2. DNS разрешение
   └─> 103.106.3.98 (IP сервера)
       │
       ▼
3. Запрос на порт 443 (HTTPS)
   └─> Caddy Server
       │
       ├─> ✅ Проверка SSL сертификата
       ├─> ✅ Проверка Rate Limit (не превышен?)
       ├─> ✅ Добавление Security Headers
       ├─> ✅ Проверка User-Agent (не бот?)
       │
       ▼
4. Проксирование на localhost:3000
   └─> Frontend контейнер (Nginx)
       │
       ├─> Если запрос к статике (/, /about, /projects):
       │   └─> Отдаёт React приложение
       │
       └─> Если запрос к API (/api/*):
           └─> proxy_pass http://backend:8080/api/
               │
               ▼
5. Backend контейнер (Spring Boot)
   └─> Обработка API запроса
       │
       ├─> Запрос к базе данных?
       │   └─> PostgreSQL (localhost:5432 внутри Docker сети)
       │
       └─> Возвращает JSON ответ
           │
           ▼
6. Ответ через цепочку обратно:
   Backend → Frontend → Caddy → Браузер
```

---

## 🔐 Какие порты открыты?

### Внешний доступ (из интернета):

| Порт | Протокол | Статус | Назначение |
|------|----------|--------|------------|
| 80   | HTTP     | ✅ Открыт | Автоматический редирект на HTTPS |
| 443  | HTTPS    | ✅ Открыт | Защищённый доступ к сайту |
| 3000 | HTTP     | 🔒 **ЗАКРЫТ** | Frontend доступен только для localhost |
| 8080 | HTTP     | 🔒 **ЗАКРЫТ** | Backend недоступен напрямую |
| 5432 | PostgreSQL | 🔒 **ЗАКРЫТ** | База данных недоступна напрямую |

### Внутренний доступ (Docker сеть):

```yaml
# docker-compose.yml

caddy:
  ports:
    - "80:80"      # Открыт в интернет
    - "443:443"    # Открыт в интернет

frontend:
  ports:
    - "127.0.0.1:3000:80"  # Доступен ТОЛЬКО для localhost (Caddy)

backend:
  expose:
    - "8080"  # Доступен только внутри Docker сети

db:
  expose:
    - "5432"  # Доступен только внутри Docker сети
```

---

## ✅ Уровни защиты

### 1️⃣ Уровень сети (Firewall)

```bash
# UFW правила
sudo ufw allow 22/tcp   # SSH
sudo ufw allow 80/tcp   # HTTP (Caddy)
sudo ufw allow 443/tcp  # HTTPS (Caddy)
sudo ufw deny 3000/tcp  # Frontend ЗАКРЫТ
sudo ufw deny 8080/tcp  # Backend ЗАКРЫТ
sudo ufw deny 5432/tcp  # PostgreSQL ЗАКРЫТ
```

### 2️⃣ Уровень Docker (Изоляция портов)

```yaml
# Порты НЕ публикуются наружу
backend:
  expose:
    - "8080"  # Только внутри Docker сети

db:
  expose:
    - "5432"  # Только внутри Docker сети

# Frontend доступен только для localhost
frontend:
  ports:
    - "127.0.0.1:3000:80"  # 127.0.0.1 = только localhost
```

**Результат:** Даже если UFW выключен, порты 8080/5432 недоступны из интернета!

### 3️⃣ Уровень Caddy (Reverse Proxy защита)

```caddyfile
portfolio.timurtm72.ru {
    # SSL терминация
    # Caddy автоматически получает Let's Encrypt сертификат

    # Rate Limiting
    rate_limit {
        zone dynamic {
            key {remote_host}
            events 100  # 100 запросов
            window 1m   # в минуту
        }
    }

    # Блокировка ботов
    @bots {
        header User-Agent *bot*
        header User-Agent *scanner*
    }
    handle @bots {
        respond "Access Denied" 403
    }

    # Security заголовки
    header {
        X-Content-Type-Options nosniff
        X-Frame-Options SAMEORIGIN
        Strict-Transport-Security "max-age=31536000"
    }

    # Проксирование на localhost:3000
    reverse_proxy localhost:3000
}
```

### 4️⃣ Уровень приложения (Spring Security)

```java
// SecurityConfig.java
http.authorizeHttpRequests(authz -> authz
    .requestMatchers("/api/skills/**").permitAll()      // Открыто
    .requestMatchers("/api/projects/**").permitAll()    // Открыто
    .requestMatchers("/api/admin/**").authenticated()   // Требует JWT
    .anyRequest().permitAll()
)
```

---

## 🧪 Проверка безопасности

### 1. Сканирование портов (с другого компьютера)

```bash
nmap -p 80,443,3000,8080,5432 103.106.3.98
```

**Ожидаемый результат:**
```
80/tcp   open     ✅
443/tcp  open     ✅
3000/tcp closed   ✅ ВАЖНО!
8080/tcp closed   ✅ ВАЖНО!
5432/tcp closed   ✅ ВАЖНО!
```

### 2. Проверка SSL

```bash
curl -I https://portfolio.timurtm72.ru
```

**Ожидаемые заголовки:**
```
HTTP/2 200
strict-transport-security: max-age=31536000; includeSubDomains; preload
x-content-type-options: nosniff
x-frame-options: SAMEORIGIN
x-xss-protection: 1; mode=block
```

### 3. Тест Rate Limiting

```bash
# Отправить 150 запросов быстро
for i in {1..150}; do
  curl -s -o /dev/null -w "%{http_code}\n" https://portfolio.timurtm72.ru/
done
```

**Результат:** После 100-го запроса должны появиться ответы `429 Too Many Requests`

### 4. Попытка прямого доступа к Backend

```bash
# Должно НЕ работать!
curl http://103.106.3.98:8080/api/projects

# Ожидается: Connection refused или timeout
```

### 5. Попытка прямого доступа к Frontend

```bash
# Должно НЕ работать!
curl http://103.106.3.98:3000/

# Ожидается: Connection refused или timeout
```

---

## 📊 Сравнение БЕЗ и С защитой

### ❌ БЕЗ Caddy (НЕЗАЩИЩЕНО):

```
docker-compose.yml:
  frontend:
    ports:
      - "3000:80"    # Открыт в интернет!

  backend:
    ports:
      - "8080:8080"  # Открыт в интернет!

  db:
    ports:
      - "5432:5432"  # Открыт в интернет!
```

**Проблемы:**
- ❌ Нет HTTPS (данные передаются открытым текстом)
- ❌ Нет Rate Limiting (легко DDoS атаковать)
- ❌ Backend доступен напрямую (можно обойти frontend логику)
- ❌ PostgreSQL доступен напрямую (риск SQL инъекций извне)
- ❌ Нет логирования атак

### ✅ С Caddy (ЗАЩИЩЕНО):

```
docker-compose.yml:
  caddy:
    ports:
      - "80:80"
      - "443:443"

  frontend:
    ports:
      - "127.0.0.1:3000:80"  # Только для localhost!

  backend:
    expose:
      - "8080"  # Только внутри Docker

  db:
    expose:
      - "5432"  # Только внутри Docker
```

**Преимущества:**
- ✅ Автоматический HTTPS
- ✅ Rate Limiting
- ✅ Backend недоступен извне
- ✅ PostgreSQL недоступен извне
- ✅ Логирование всех запросов
- ✅ Блокировка ботов
- ✅ Security заголовки

---

## 🎓 Ключевые концепции

### localhost vs 0.0.0.0

```yaml
# ❌ ПЛОХО: Доступен из интернета
ports:
  - "3000:80"
  - "0.0.0.0:3000:80"

# ✅ ХОРОШО: Доступен только с сервера
ports:
  - "127.0.0.1:3000:80"
```

### ports vs expose

```yaml
# ports: публикует порт наружу (опасно!)
ports:
  - "8080:8080"  # Доступен из интернета

# expose: доступен только внутри Docker сети (безопасно)
expose:
  - "8080"  # Доступен только для других контейнеров
```

### Docker сеть portfolio-network

Все контейнеры находятся в одной изолированной сети:

```
portfolio-network (bridge):
  - caddy (может обращаться к localhost:3000 хост-машины)
  - frontend (может обращаться к backend:8080)
  - backend (может обращаться к db:5432)
  - db (изолирован)
```

**Важно:** Контейнеры видят друг друга по именам (`backend:8080`, `db:5432`), но извне доступен только Caddy!

---

## 📚 Дополнительная документация

- **Быстрый старт:** [QUICK-DEPLOY-GUIDE.md](QUICK-DEPLOY-GUIDE.md)
- **Настройка Caddy:** [deploy/CADDY-SETUP.md](deploy/CADDY-SETUP.md)
- **Деплой через GitHub:** [deploy/GITHUB-DEPLOY-CADDY.md](deploy/GITHUB-DEPLOY-CADDY.md)
- **Полная документация:** [CLAUDE.md](CLAUDE.md)
