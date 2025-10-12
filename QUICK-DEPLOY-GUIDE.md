# Быстрый гид по деплою портфолио с Caddy

## 📝 Шаг 1: Подготовка на вашем компьютере (Windows)

### 1.1. Настройте Caddyfile

```bash
# Откройте в редакторе
Caddyfile
```

**Замените:**
- `email timurtm72@example.com` → ваш реальный email
- `portfolio.timurtm72.ru` → ваш домен (если есть)

**Если домена нет:**
- Раскомментируйте секцию `http://103.106.3.98 {` в конце файла
- Закомментируйте секцию с `portfolio.timurtm72.ru {`

---

## 📤 Шаг 2: Закоммитьте в GitHub

```bash
# Добавить новые файлы
git add Caddyfile docker-compose.yml deploy/

# Проверить что .env НЕ добавлен
git status
# Если видите backend/.env - НЕ коммитьте его!

# Создать коммит
git commit -m "feat: добавлена защита приложения с Caddy Reverse Proxy

- Автоматический HTTPS (Let's Encrypt)
- Rate limiting 100 запросов/минуту
- Security headers
- Порты 3000/8080/5432 закрыты от интернета
- Блокировка ботов и сканеров
"

# Отправить в GitHub
git push origin main
```

---

## 🌐 Шаг 3: Настройка DNS (если есть домен)

Зайдите в панель управления доменом и добавьте:

```
Тип: A
Имя: @ (или subdomain)
Значение: 103.106.3.98
TTL: 300
```

**Проверка:**
```bash
nslookup ваш-домен.com
# Должен вернуть: 103.106.3.98
```

⏳ Подождите 5-15 минут (DNS propagation)

---

## 🖥️ Шаг 4: Деплой на Orange Pi

### 4.1. SSH подключение

```bash
ssh orangepi@103.106.3.98
```

### 4.2. Обновление и деплой

```bash
# Перейти в папку проекта
cd /opt/portfolio  # Или где у вас репозиторий

# Обновить из GitHub
git pull origin main

# ⚠️ ВАЖНО: Создать .env файлы вручную!
nano backend/.env
# Вставьте:
DB_HOST=db
DB_PORT=5432
DB_USER=portfolio_user
DB_PASSWORD=ваш_надежный_пароль_БД
ADMIN_USERNAME=admin
ADMIN_PASSWORD=ваш_надежный_пароль_админа
JWT_SECRET=$(openssl rand -base64 64 | head -1)
CORS_ALLOWED_ORIGINS=https://ваш-домен.com,http://localhost:3000

nano frontend/.env
# Вставьте:
REACT_APP_API_URL=/api

# Открыть порты в firewall
sudo ufw allow 22/tcp   # SSH
sudo ufw allow 80/tcp   # HTTP
sudo ufw allow 443/tcp  # HTTPS
sudo ufw deny 3000/tcp  # Закрыть старый порт!
sudo ufw deny 8080/tcp
sudo ufw deny 5432/tcp
sudo ufw --force enable

# Запустить автоматический деплой
chmod +x deploy/caddy-deploy.sh
bash deploy/caddy-deploy.sh
```

⏳ **Ждем 30-60 минут** (первая сборка на Orange Pi медленная)

---

## ✅ Шаг 5: Проверка

### 5.1. Проверить контейнеры

```bash
docker compose ps
```

Должны быть:
```
portfolio-caddy          Up (healthy)
portfolio-frontend       Up
portfolio-backend        Up (healthy)
portfolio-db             Up (healthy)
```

### 5.2. Открыть в браузере

**С доменом:**
```
https://ваш-домен.com
```

**Без домена:**
```
http://103.106.3.98
```

### 5.3. Проверить безопасность

```bash
# Сканирование портов (с другого компьютера)
nmap -p 80,443,3000,8080,5432 103.106.3.98
```

Должно быть:
```
80/tcp   open     ✅
443/tcp  open     ✅
3000/tcp closed   ✅ (ВАЖНО!)
8080/tcp closed   ✅ (ВАЖНО!)
5432/tcp closed   ✅ (ВАЖНО!)
```

---

## 🔄 Обновление в будущем

### На вашем компьютере:
```bash
# Внести изменения в код
# ...

# Закоммитить
git add .
git commit -m "feat: описание изменений"
git push origin main
```

### На Orange Pi:
```bash
cd /opt/portfolio
git pull origin main
bash deploy/caddy-deploy.sh
```

---

## 🆘 Если что-то пошло не так

### Логи контейнеров:
```bash
docker compose logs -f caddy
docker compose logs -f backend
docker compose logs -f frontend
```

### Перезапуск:
```bash
docker compose restart
```

### Полная пересборка:
```bash
docker compose down
docker compose build --no-cache
docker compose up -d
```

### SSL сертификат не получен:
```bash
# Проверить DNS
nslookup ваш-домен.com

# Проверить логи Caddy
docker compose logs caddy | grep -i "acme\|certificate"

# Подождать 5-15 минут
```

---

## 📚 Полная документация

- [deploy/GITHUB-DEPLOY-CADDY.md](deploy/GITHUB-DEPLOY-CADDY.md) - пошаговая инструкция
- [deploy/CADDY-SETUP.md](deploy/CADDY-SETUP.md) - подробная настройка Caddy
- [CLAUDE.md](CLAUDE.md) - полная документация проекта

---

## 🎉 Готово!

После успешного деплоя:
- ✅ Приложение доступно по HTTPS
- ✅ Порты 3000/8080/5432 закрыты
- ✅ Rate limiting включен
- ✅ Security headers установлены
- ✅ Защита от ботов активна

**Ваше портфолио защищено! 🔒**
