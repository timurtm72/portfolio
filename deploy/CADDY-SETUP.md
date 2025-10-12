# Развертывание Portfolio с Caddy Reverse Proxy

## 🔒 Архитектура безопасности

```
Интернет (порт 80/443)
    ↓
[Caddy Server] ← Единственная точка входа
    ├─ Автоматический HTTPS (Let's Encrypt)
    ├─ Rate Limiting (100 req/min)
    ├─ Security Headers
    ├─ Логирование атак
    └─ Reverse Proxy ↓
         ↓
    [Frontend:80] → [Backend:8080] → [PostgreSQL:5432]
    (внутренние порты, НЕ доступны из интернета)
```

**Защищено:**
- ✅ Порт 3000/80 (frontend) - доступен только через Caddy
- ✅ Порт 8080 (backend) - доступен только внутри Docker сети
- ✅ Порт 5432 (PostgreSQL) - доступен только внутри Docker сети
- ✅ Автоматический HTTPS с Let's Encrypt
- ✅ Rate limiting 100 запросов/минуту
- ✅ Блокировка ботов и сканеров
- ✅ Security заголовки (XSS, Clickjacking защита)

---

## 📋 Предварительные требования

1. **Домен** (обязательно для автоматического SSL):
   - Купите домен (Cloudflare, Namecheap, Reg.ru, etc.)
   - Настройте DNS A-запись на IP сервера: `103.106.3.98`

2. **Открытые порты на сервере/роутере**:
   ```bash
   # Проверить открытые порты
   sudo ufw status

   # Открыть порты 80 и 443
   sudo ufw allow 80/tcp
   sudo ufw allow 443/tcp

   # ⚠️ ЗАКРЫТЬ старые порты (если были открыты)
   sudo ufw deny 3000/tcp
   sudo ufw deny 8080/tcp
   sudo ufw deny 5432/tcp
   ```

3. **Docker и Docker Compose** установлены:
   ```bash
   docker --version
   docker compose version
   ```

---

## 🚀 Быстрый старт (Orange Pi Zero 3)

### Вариант 1: Автоматический деплой через GitHub

```bash
# На Orange Pi / сервере
cd /opt/portfolio  # Или куда клонирован репозиторий

# Обновить из GitHub
git pull origin main

# Запустить деплой скрипт (создадим ниже)
bash deploy/caddy-deploy.sh
```

### Вариант 2: Ручной деплой

```bash
# 1. Клонировать репозиторий
git clone https://github.com/timurtm72/portfolio.git
cd portfolio

# 2. Настроить переменные окружения
cp backend/env.example backend/.env
cp frontend/env.example frontend/.env

# Отредактировать backend/.env
nano backend/.env
# Измените:
# - DB_PASSWORD (используйте надежный пароль!)
# - ADMIN_PASSWORD (НЕ используйте admin123!)
# - JWT_SECRET (сгенерируйте: openssl rand -base64 64)

# 3. Настроить Caddyfile
nano Caddyfile
# Измените строку:
# portfolio.timurtm72.ru → ваш-домен.com

# Укажите ваш email для Let's Encrypt:
# email your-email@example.com

# 4. Остановить старые контейнеры (если запущены)
docker compose down

# 5. Собрать и запустить
docker compose build
docker compose up -d

# 6. Проверить статус
docker compose ps
docker compose logs -f caddy
```

---

## 🔧 Настройка Caddyfile

### Production (с доменом и автоматическим SSL)

Откройте [Caddyfile](../Caddyfile):

```bash
nano Caddyfile
```

Измените:
```caddyfile
# Email для Let's Encrypt
{
    email ваш-email@example.com  # Замените!
}

# Ваш домен
portfolio.timurtm72.ru {  # Замените на ваш домен!
    # ... остальное оставьте как есть
}
```

### Локальная разработка (без SSL)

Раскомментируйте секцию в Caddyfile:
```caddyfile
# Отключить автоматический HTTPS
{
    auto_https off
}

http://localhost {
    reverse_proxy frontend:80
}
```

### IP без домена (без SSL)

Раскомментируйте секцию:
```caddyfile
http://103.106.3.98 {
    header {
        X-Content-Type-Options nosniff
        X-Frame-Options SAMEORIGIN
    }

    rate_limit {
        zone dynamic {
            key {remote_host}
            events 100
            window 1m
        }
    }

    reverse_proxy frontend:80
}
```

---

## 🔐 Дополнительная защита

### 1. Защита админских эндпоинтов (IP whitelist)

Узнайте ваш текущий IP:
```bash
curl ifconfig.me
```

Откройте Caddyfile:
```caddyfile
# Раскомментируйте эту секцию:
handle @admin_paths {
    @allowed {
        remote_ip 203.0.113.1  # Замените на ваш реальный IP!
    }
    handle @allowed {
        reverse_proxy frontend:80
    }
    respond "Forbidden" 403
}
```

Перезапустите Caddy:
```bash
docker compose restart caddy
```

### 2. Изменить лимит запросов

В Caddyfile измените:
```caddyfile
rate_limit {
    zone dynamic {
        key {remote_host}
        events 50   # Уменьшите до 50 запросов/минуту
        window 1m
    }
}
```

### 3. Добавить блокировку по географии (требует плагин)

Для продвинутой блокировки используйте [Caddy GeoIP плагин](https://github.com/shift72/caddy-geo).

---

## 📊 Мониторинг и логи

### Проверить статус контейнеров
```bash
docker compose ps
```

Ожидаемый вывод:
```
NAME                     STATUS       PORTS
portfolio-caddy          Up (healthy) 0.0.0.0:80->80/tcp, 0.0.0.0:443->443/tcp
portfolio-frontend       Up           80/tcp
portfolio-backend        Up (healthy) 8080/tcp
portfolio-db             Up (healthy) 5432/tcp
```

### Логи Caddy
```bash
# Реал-тайм логи
docker compose logs -f caddy

# Последние 100 строк
docker compose logs --tail=100 caddy

# Логи из файла (внутри контейнера)
docker exec portfolio-caddy cat /var/log/caddy/portfolio-access.log
```

### Проверить SSL сертификат
```bash
# Проверить что сертификат получен
docker exec portfolio-caddy ls -la /data/caddy/certificates

# Информация о сертификате
openssl s_client -connect ваш-домен.com:443 -showcerts
```

### Тест производительности
```bash
# Установить ApacheBench
sudo apt install apache2-utils -y

# Тест 1000 запросов, 10 одновременно
ab -n 1000 -c 10 https://ваш-домен.com/
```

### Мониторинг атак
```bash
# Фильтр по статусам ошибок
docker exec portfolio-caddy grep '"status":403' /var/log/caddy/portfolio-access.log

# Подсчет заблокированных запросов
docker exec portfolio-caddy grep '"status":403' /var/log/caddy/portfolio-access.log | wc -l

# Топ IP адресов с ошибками
docker exec portfolio-caddy grep '"status":403' /var/log/caddy/portfolio-access.log | \
  grep -oP '"remote_ip":"[^"]*"' | sort | uniq -c | sort -rn | head -10
```

---

## 🐛 Решение проблем

### Проблема: Caddy не получает SSL сертификат

**Причина:** DNS не указывает на сервер или порт 80 закрыт.

**Решение:**
```bash
# 1. Проверить DNS
nslookup ваш-домен.com
# Должен показать IP: 103.106.3.98

# 2. Проверить порты
sudo netstat -tulpn | grep ':80\|:443'

# 3. Проверить логи Caddy
docker compose logs caddy | grep -i "acme\|certificate\|error"

# 4. Если домен новый - подождите 5-15 минут (DNS propagation)

# 5. Форсировать обновление сертификата
docker compose restart caddy
```

### Проблема: 502 Bad Gateway

**Причина:** Frontend контейнер не запущен или не прошел healthcheck.

**Решение:**
```bash
# Проверить статус
docker compose ps

# Логи frontend
docker compose logs frontend

# Перезапустить
docker compose restart frontend
```

### Проблема: Rate limit срабатывает слишком часто

**Решение:** Увеличьте лимит в Caddyfile:
```caddyfile
rate_limit {
    zone dynamic {
        key {remote_host}
        events 200   # Увеличить до 200
        window 1m
    }
}
```

### Проблема: Не работает из локальной сети

**Причина:** Caddy использует HTTPS, а браузер не доверяет самоподписанному сертификату в локальной сети.

**Решение:** Используйте домен или настройте локальный Caddyfile:
```caddyfile
http://192.168.1.100 {  # IP вашего Orange Pi в локальной сети
    reverse_proxy frontend:80
}
```

---

## 🔄 Обновление приложения

### Обновление через GitHub
```bash
cd /opt/portfolio
git pull origin main
docker compose down
docker compose build --no-cache
docker compose up -d
```

### Обновление только Caddyfile (без пересборки)
```bash
nano Caddyfile
# Внесите изменения

# Перезагрузить конфигурацию (без downtime!)
docker exec portfolio-caddy caddy reload --config /etc/caddy/Caddyfile
```

### Резервное копирование SSL сертификатов
```bash
# Создать бэкап
docker run --rm -v portfolio_caddy_data:/data \
  -v $(pwd):/backup alpine \
  tar czf /backup/caddy-certs-backup.tar.gz /data/caddy/certificates

# Восстановить бэкап
docker run --rm -v portfolio_caddy_data:/data \
  -v $(pwd):/backup alpine \
  tar xzf /backup/caddy-certs-backup.tar.gz -C /
```

---

## 🧪 Тестирование безопасности

### Проверить заголовки безопасности
```bash
curl -I https://ваш-домен.com
```

Ожидаемые заголовки:
```
Strict-Transport-Security: max-age=31536000; includeSubDomains; preload
X-Content-Type-Options: nosniff
X-Frame-Options: SAMEORIGIN
X-XSS-Protection: 1; mode=block
```

### Тест SSL конфигурации
Используйте [SSL Labs](https://www.ssllabs.com/ssltest/) для анализа HTTPS конфигурации.

### Тест Rate Limiting
```bash
# Отправить 150 запросов быстро
for i in {1..150}; do
  curl -s -o /dev/null -w "%{http_code}\n" https://ваш-домен.com/
done

# После 100-го запроса должны быть ответы 429 (Too Many Requests)
```

### Сканирование портов
```bash
# Проверить что закрыты порты 3000, 8080, 5432
nmap -p 80,443,3000,8080,5432 103.106.3.98
```

Ожидаемый вывод:
```
PORT     STATE
80/tcp   open
443/tcp  open
3000/tcp closed  ← Должен быть closed!
8080/tcp closed  ← Должен быть closed!
5432/tcp closed  ← Должен быть closed!
```

---

## 📚 Полезные ссылки

- [Caddy документация](https://caddyserver.com/docs/)
- [Caddyfile синтаксис](https://caddyserver.com/docs/caddyfile)
- [Rate Limiting модуль](https://github.com/mholt/caddy-ratelimit)
- [Security заголовки best practices](https://owasp.org/www-project-secure-headers/)
- [Let's Encrypt лимиты](https://letsencrypt.org/docs/rate-limits/)

---

## ⚠️ Важные замечания

1. **Домен обязателен для автоматического SSL**
   Без домена используйте IP + HTTP или купите домен (~$10/год).

2. **DNS Propagation занимает время**
   После настройки DNS подождите 5-15 минут перед первым запуском.

3. **Let's Encrypt имеет лимиты**
   50 сертификатов на домен в неделю. Не пересоздавайте контейнеры слишком часто!

4. **Резервное копирование сертификатов**
   Volume `caddy_data` содержит SSL сертификаты. Делайте бэкапы!

5. **Логи содержат IP адреса**
   Соблюдайте GDPR/privacy при хранении логов с IP.

6. **Rate Limiting глобальный**
   100 req/min применяется ко ВСЕМ пользователям с одного IP.
   В локальной сети за NAT все будут иметь один внешний IP!

7. **Не коммитьте .env файлы в GitHub**
   Они содержат пароли БД и JWT секреты!

---

## 📝 Чеклист перед production

- [ ] Домен настроен и указывает на сервер
- [ ] Порты 80/443 открыты в firewall
- [ ] Старые порты 3000/8080/5432 ЗАКРЫТЫ
- [ ] Email в Caddyfile изменен на реальный
- [ ] Домен в Caddyfile изменен на ваш
- [ ] backend/.env содержит надежные пароли (НЕ admin123!)
- [ ] JWT_SECRET сгенерирован случайно (openssl rand -base64 64)
- [ ] .env файлы добавлены в .gitignore
- [ ] SSL сертификат получен успешно (проверить логи)
- [ ] Тест: curl -I https://домен показывает security headers
- [ ] Тест: nmap показывает что 3000/8080/5432 закрыты
- [ ] Резервная копия caddy_data создана
- [ ] Мониторинг логов настроен (optional)

**После всех проверок:** Приложение готово к production! 🚀
