# Деплой портфолио БЕЗ домена (только IP)

## ⚠️ Важно понимать

**БЕЗ домена:**
- ❌ Нет HTTPS (данные передаются HTTP без шифрования)
- ❌ Браузеры покажут "Не защищено"
- ✅ Всё остальное работает: Rate Limiting, Security Headers, защита портов

**Если нужен HTTPS - купите домен (~$10/год):**
- [Cloudflare Registrar](https://www.cloudflare.com/products/registrar/) - $8-10/год
- [Namecheap](https://www.namecheap.com/) - $9-12/год
- [Reg.ru](https://www.reg.ru/) - 600-800₽/год

---

## 🔒 Архитектура (БЕЗ домена)

```
Браузер пользователя
    ↓
http://103.106.3.98:80 (HTTP без SSL) ⚠️
    ↓
[Caddy Server]
  ✅ Rate Limiting (100 req/min)
  ✅ Security Headers
  ✅ Блокировка ботов
  ❌ НЕТ HTTPS (нет домена)
    ↓
reverse_proxy localhost:3000
    ↓
[Frontend - порт 3000] ← Доступен только для Caddy
    ↓
[Backend - порт 8080] ← ЗАКРЫТ от интернета
    ↓
[PostgreSQL - порт 5432] ← ЗАКРЫТ от интернета
```

---

## 📝 Шаг 1: Подготовка (на вашем компьютере)

### 1.1. Caddyfile уже настроен!

Файл [Caddyfile](../Caddyfile) уже сконфигурирован для работы по IP `103.106.3.98`.

**Проверьте что активна секция:**
```caddyfile
http://103.106.3.98 {
    # ... настройки
}
```

И **закомментирована** секция с доменом (строки 103-172).

### 1.2. Настройте .env файлы

```bash
# НЕ коммитьте .env в GitHub!
# Они будут созданы вручную на сервере
```

### 1.3. Закоммитьте изменения

```bash
git add Caddyfile docker-compose.yml deploy/
git commit -m "feat: настроена защита портфолио с Caddy (без домена, HTTP)"
git push origin main
```

---

## 🖥️ Шаг 2: Деплой на Orange Pi

### 2.1. SSH подключение

```bash
ssh orangepi@103.106.3.98
```

### 2.2. Обновить репозиторий

```bash
cd /opt/portfolio
git pull origin main
```

### 2.3. Создать .env файлы

**⚠️ КРИТИЧНО! .env не закоммичены в Git!**

```bash
# Backend .env
nano backend/.env
```

Вставьте:
```bash
DB_HOST=db
DB_PORT=5432
DB_USER=portfolio_user
DB_PASSWORD=ПРИДУМАЙТЕ_НАДЕЖНЫЙ_ПАРОЛЬ_БД

ADMIN_USERNAME=admin
ADMIN_PASSWORD=ПРИДУМАЙТЕ_НАДЕЖНЫЙ_ПАРОЛЬ_АДМИНА

# Сгенерируйте JWT секрет:
JWT_SECRET=$(openssl rand -base64 64 | head -1)

# Важно: используйте http:// (без s) т.к. нет домена
CORS_ALLOWED_ORIGINS=http://103.106.3.98,http://localhost:3000
```

```bash
# Frontend .env
nano frontend/.env
```

Вставьте:
```bash
REACT_APP_API_URL=/api
```

### 2.4. Настроить firewall

```bash
# Открыть нужные порты
sudo ufw allow 22/tcp   # SSH (ОБЯЗАТЕЛЬНО!)
sudo ufw allow 80/tcp   # HTTP (Caddy)

# ЗАКРЫТЬ прямой доступ к портам приложения
sudo ufw deny 3000/tcp  # Frontend
sudo ufw deny 8080/tcp  # Backend
sudo ufw deny 5432/tcp  # PostgreSQL

# Включить firewall
sudo ufw --force enable

# Проверить
sudo ufw status
```

**Ожидаемый вывод:**
```
Status: active

To                         Action      From
--                         ------      ----
22/tcp                     ALLOW       Anywhere
80/tcp                     ALLOW       Anywhere
3000/tcp                   DENY        Anywhere
8080/tcp                   DENY        Anywhere
5432/tcp                   DENY        Anywhere
```

### 2.5. Запустить деплой

```bash
chmod +x deploy/caddy-deploy.sh
bash deploy/caddy-deploy.sh
```

⏳ **Ожидание:** 30-60 минут (первая сборка на Orange Pi)

Скрипт автоматически:
- Проверит окружение
- Соберёт Docker образы
- Запустит контейнеры
- Проверит работоспособность

---

## ✅ Шаг 3: Проверка

### 3.1. Проверить контейнеры

```bash
docker compose ps
```

Должны быть запущены:
```
NAME                     STATUS       PORTS
portfolio-caddy          Up (healthy) 0.0.0.0:80->80/tcp
portfolio-frontend       Up           127.0.0.1:3000->80/tcp
portfolio-backend        Up (healthy) 8080/tcp
portfolio-db             Up (healthy) 5432/tcp
```

### 3.2. Открыть в браузере

```
http://103.106.3.98
```

⚠️ Браузер покажет "Не защищено" - это нормально без домена!

### 3.3. Проверить логи

```bash
# Логи Caddy
docker compose logs -f caddy

# Логи всех сервисов
docker compose logs -f
```

### 3.4. Проверить безопасность портов (ВАЖНО!)

**С другого компьютера** (не с Orange Pi):

```bash
nmap -p 80,443,3000,8080,5432 103.106.3.98
```

**Ожидаемый результат:**
```
80/tcp   open     ✅ Caddy HTTP
443/tcp  closed   ✅ HTTPS не используется (нет домена)
3000/tcp closed   ✅ ВАЖНО! Frontend недоступен напрямую
8080/tcp closed   ✅ ВАЖНО! Backend недоступен напрямую
5432/tcp closed   ✅ ВАЖНО! PostgreSQL недоступен напрямую
```

**Если 3000/8080/5432 показывают `open` - это проблема безопасности!**

---

## 🧪 Тестирование защиты

### 1. Проверить Security заголовки

```bash
curl -I http://103.106.3.98
```

Должны быть:
```
X-Content-Type-Options: nosniff
X-Frame-Options: SAMEORIGIN
X-XSS-Protection: 1; mode=block
```

### 2. Проверить Rate Limiting

```bash
# Отправить 150 запросов быстро
for i in {1..150}; do
  curl -s -o /dev/null -w "%{http_code}\n" http://103.106.3.98/
done
```

Результат: После 100-го запроса должны появиться ответы `429 Too Many Requests`

### 3. Попытка прямого доступа к Backend

```bash
# Должно НЕ работать (Connection refused)
curl http://103.106.3.98:8080/api/projects
```

### 4. Попытка прямого доступа к Frontend

```bash
# Должно НЕ работать (Connection refused)
curl http://103.106.3.98:3000/
```

---

## 🔄 Обновление в будущем

### На вашем компьютере:
```bash
# Внести изменения
git add .
git commit -m "feat: описание"
git push origin main
```

### На Orange Pi:
```bash
cd /opt/portfolio
git pull origin main
bash deploy/caddy-deploy.sh
```

---

## 🔧 Решение проблем

### Проблема: Порт 80 занят

```bash
# Найти процесс
sudo lsof -i :80

# Остановить Apache/Nginx
sudo systemctl stop apache2
sudo systemctl disable apache2
```

### Проблема: Порты 3000/8080/5432 открыты (nmap показывает open)

```bash
# Проверить firewall
sudo ufw status

# Закрыть порты
sudo ufw deny 3000/tcp
sudo ufw deny 8080/tcp
sudo ufw deny 5432/tcp

# Проверить docker-compose.yml
# Frontend должен быть: 127.0.0.1:3000:80
# Backend должен быть: expose: "8080"
# DB должен быть: expose: "5432"

# Пересоздать контейнеры
docker compose down
docker compose up -d
```

### Проблема: Сайт не открывается

```bash
# Проверить логи
docker compose logs caddy
docker compose logs frontend
docker compose logs backend

# Проверить что порт 80 открыт
sudo ufw status | grep 80

# Перезапустить
docker compose restart
```

---

## 🎯 Что защищено (БЕЗ домена)

| Защита | Статус |
|--------|--------|
| HTTPS шифрование | ❌ Нет (требуется домен) |
| Rate Limiting | ✅ Да (100 req/min) |
| Security Headers | ✅ Да |
| Блокировка ботов | ✅ Да |
| Порт 3000 закрыт | ✅ Да (только localhost) |
| Порт 8080 закрыт | ✅ Да (только Docker сеть) |
| Порт 5432 закрыт | ✅ Да (только Docker сеть) |
| Логирование | ✅ Да |

---

## 💡 Хотите HTTPS?

Купите домен (~$10/год) и:

1. Настройте DNS: `ваш-домен.com` → `103.106.3.98`
2. Откройте [Caddyfile](../Caddyfile)
3. Раскомментируйте секцию с доменом (строки 103-172)
4. Закомментируйте секцию с IP (строки 13-101)
5. Измените `auto_https off` на `auto_https on`
6. Укажите ваш email для Let's Encrypt
7. Перезапустите: `docker compose restart caddy`

Caddy автоматически получит SSL сертификат за 1-2 минуты!

---

## 📚 Дополнительная документация

- [ARCHITECTURE.md](../ARCHITECTURE.md) - детальная схема безопасности
- [CADDY-SETUP.md](CADDY-SETUP.md) - полная настройка Caddy
- [QUICK-DEPLOY-GUIDE.md](../QUICK-DEPLOY-GUIDE.md) - быстрый гид
- [CLAUDE.md](../CLAUDE.md) - документация проекта

---

## ✅ Итоговый чеклист

- [ ] Caddyfile настроен для IP (http://103.106.3.98)
- [ ] Изменения закоммичены в GitHub
- [ ] .env файлы созданы на сервере (НЕ в GitHub!)
- [ ] Firewall настроен (80 открыт, 3000/8080/5432 закрыты)
- [ ] Скрипт деплоя выполнен успешно
- [ ] Контейнеры запущены (docker compose ps)
- [ ] Сайт открывается: http://103.106.3.98
- [ ] nmap показывает 3000/8080/5432 closed
- [ ] Rate limiting работает (тест 150 запросов)

**Если все чекбоксы отмечены - приложение защищено! 🚀**

(Кроме HTTPS - для этого нужен домен)
