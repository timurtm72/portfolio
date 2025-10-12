# Быстрый деплой с Caddy через GitHub

## 🎯 Для Orange Pi Zero 3 с доступом через GitHub

Эта инструкция для случая когда у вас есть только SSH доступ к серверу и Git репозиторий.

---

## Шаг 1: Подготовка на локальной машине

### 1.1. Настроить Caddyfile (ОБЯЗАТЕЛЬНО!)

```bash
# Открыть Caddyfile
nano Caddyfile

# Заменить строки:
email your-email@example.com  # Ваш реальный email для Let's Encrypt
portfolio.timurtm72.ru {      # Ваш реальный домен!
```

**Важно:** Если у вас нет домена - инструкция ниже!

### 1.2. Настроить .env файлы (ОБЯЗАТЕЛЬНО!)

```bash
# Backend
cp backend/env.example backend/.env
nano backend/.env
```

**Измените в backend/.env:**
```bash
# ⚠️ НЕ используйте эти значения! Придумайте свои!
DB_PASSWORD=ваш_надежный_пароль_минимум_20_символов
ADMIN_USERNAME=ваш_логин
ADMIN_PASSWORD=ваш_надежный_пароль_админа
JWT_SECRET=$(openssl rand -base64 64)  # Сгенерировать случайно!
```

```bash
# Frontend
cp frontend/env.example frontend/.env
# Можно оставить как есть для production
```

### 1.3. Добавить .env в .gitignore

```bash
# Проверить что .env НЕ закоммичены!
git status

# Если видите backend/.env или frontend/.env:
echo "backend/.env" >> .gitignore
echo "frontend/.env" >> .gitignore

git add .gitignore
git commit -m "chore: добавлен .gitignore для .env файлов"
```

### 1.4. Закоммитить изменения

```bash
git add Caddyfile docker-compose.yml deploy/
git commit -m "feat: добавлена интеграция Caddy для защиты приложения"
git push origin main
```

---

## Шаг 2: Настройка DNS (КРИТИЧНО!)

### 2.1. Если у вас есть домен

Зайдите в панель управления доменом (Cloudflare, Reg.ru, etc.) и добавьте A-запись:

```
Тип: A
Имя: @ (или portfolio)
Значение: 103.106.3.98  # IP вашего Orange Pi
TTL: 300 (5 минут)
```

**Проверка DNS:**
```bash
nslookup ваш-домен.com
# Должен вернуть: 103.106.3.98
```

⏳ Ожидайте 5-15 минут (DNS propagation)

### 2.2. Если домена НЕТ

**Вариант A: Использовать IP без SSL**

Откройте Caddyfile и замените секцию:
```caddyfile
# Закомментируйте секцию с доменом:
# portfolio.timurtm72.ru {
#   ...
# }

# Раскомментируйте секцию с IP:
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

**Вариант B: Купить дешевый домен**

Домены стоят ~$10/год:
- [Cloudflare Registrar](https://www.cloudflare.com/products/registrar/) (~$8-10/год)
- [Namecheap](https://www.namecheap.com/) (~$9-12/год)
- [Reg.ru](https://www.reg.ru/) (~600-800₽/год)

---

## Шаг 3: Деплой на Orange Pi

### 3.1. SSH подключение

```bash
ssh orangepi@103.106.3.98
# Или ваш пользователь и IP
```

### 3.2. Клонировать/обновить репозиторий

**Если первый раз:**
```bash
cd /opt
sudo mkdir -p portfolio
sudo chown $USER:$USER portfolio
git clone https://github.com/timurtm72/portfolio.git portfolio
cd portfolio
```

**Если уже клонирован:**
```bash
cd /opt/portfolio  # Или где у вас репозиторий
git pull origin main
```

### 3.3. Создать .env файлы (КРИТИЧНО!)

⚠️ **ВАЖНО:** .env файлы НЕ закоммичены в GitHub (безопасность!)

Создайте их вручную на сервере:

```bash
# Backend .env
nano backend/.env
```

Вставьте (замените на ВАШИ значения!):
```bash
DB_HOST=db
DB_PORT=5432
DB_USER=portfolio_user
DB_PASSWORD=ваш_надежный_пароль_БД

ADMIN_USERNAME=ваш_логин_админа
ADMIN_PASSWORD=ваш_надежный_пароль_админа

# Сгенерируйте JWT секрет:
# openssl rand -base64 64
JWT_SECRET=ваш_случайно_сгенерированный_jwt_секрет

CORS_ALLOWED_ORIGINS=https://ваш-домен.com,http://localhost:3000
```

```bash
# Frontend .env
nano frontend/.env
```

Вставьте:
```bash
REACT_APP_API_URL=/api
```

### 3.4. Открыть порты в firewall

```bash
# Установить UFW (если нет)
sudo apt install ufw -y

# Открыть нужные порты
sudo ufw allow 22/tcp   # SSH (ОБЯЗАТЕЛЬНО!)
sudo ufw allow 80/tcp   # HTTP
sudo ufw allow 443/tcp  # HTTPS

# ЗАКРЫТЬ старые порты (если были открыты)
sudo ufw deny 3000/tcp
sudo ufw deny 8080/tcp
sudo ufw deny 5432/tcp

# Включить firewall
sudo ufw --force enable

# Проверить
sudo ufw status
```

### 3.5. Запустить автоматический деплой

```bash
# Сделать скрипт исполняемым
chmod +x deploy/caddy-deploy.sh

# Запустить деплой
bash deploy/caddy-deploy.sh
```

⏳ **Ожидание:** Первая сборка займет 30-60 минут на Orange Pi!

Скрипт автоматически:
- ✅ Проверит окружение
- ✅ Соберет Docker образы
- ✅ Запустит контейнеры
- ✅ Проверит работоспособность
- ✅ Получит SSL сертификат (если домен настроен)

---

## Шаг 4: Проверка работы

### 4.1. Проверить статус контейнеров

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

### 4.2. Проверить логи

```bash
# Логи Caddy
docker compose logs -f caddy

# Если видите ошибки SSL:
docker compose logs caddy | grep -i "acme\|certificate\|error"
```

### 4.3. Открыть сайт в браузере

**С доменом:**
```
https://ваш-домен.com
```

**Без домена (только IP):**
```
http://103.106.3.98
```

### 4.4. Проверить SSL сертификат

```bash
# В браузере нажмите на замок в адресной строке
# Должно быть: Let's Encrypt сертификат

# Или командой:
openssl s_client -connect ваш-домен.com:443 -showcerts
```

---

## Шаг 5: Настройка автообновления (опционально)

### Вариант 1: Создать скрипт обновления

На Orange Pi создайте файл:
```bash
nano /home/orangepi/update-portfolio.sh
```

Содержимое:
```bash
#!/bin/bash
cd /opt/portfolio
git pull origin main
bash deploy/caddy-deploy.sh
```

Сделайте исполняемым:
```bash
chmod +x /home/orangepi/update-portfolio.sh
```

Теперь для обновления просто запускайте:
```bash
~/update-portfolio.sh
```

### Вариант 2: Настроить автообновление через cron

```bash
crontab -e
```

Добавьте строку (обновление каждую ночь в 3:00):
```cron
0 3 * * * cd /opt/portfolio && git pull && bash deploy/caddy-deploy.sh >> /var/log/portfolio-deploy.log 2>&1
```

---

## 🔧 Частые проблемы

### Проблема 1: SSL сертификат не получен

**Причина:** DNS не настроен или порт 80 закрыт.

**Решение:**
```bash
# Проверить DNS
nslookup ваш-домен.com

# Проверить порт 80
sudo netstat -tulpn | grep ':80'

# Проверить логи Caddy
docker compose logs caddy | grep -i "acme"

# Подождать 5-15 минут (DNS propagation)
```

### Проблема 2: Порт 80 занят

**Причина:** Запущен другой веб-сервер (Apache/Nginx).

**Решение:**
```bash
# Найти процесс
sudo lsof -i :80

# Остановить Apache
sudo systemctl stop apache2
sudo systemctl disable apache2

# Остановить Nginx
sudo systemctl stop nginx
sudo systemctl disable nginx

# Перезапустить Caddy
docker compose restart caddy
```

### Проблема 3: .env файлы не найдены

**Причина:** .env не закоммичены в Git (это правильно!).

**Решение:** Создайте их вручную на сервере (см. Шаг 3.3)

### Проблема 4: Backend не запускается

**Причина:** Неправильный пароль БД или отсутствует JWT_SECRET.

**Решение:**
```bash
# Проверить логи
docker compose logs backend

# Проверить .env
cat backend/.env

# Пересоздать контейнеры
docker compose down
docker compose up -d
```

### Проблема 5: WiFi отваливается на Orange Pi

**Решение:** См. [SSH-WIFI-SETUP.md](SSH-WIFI-SETUP.md)

---

## 📊 Мониторинг после деплоя

### Проверить безопасность

```bash
# Сканирование портов
nmap -p 80,443,3000,8080,5432 103.106.3.98

# Должно быть:
# 80/tcp   open     ✅
# 443/tcp  open     ✅
# 3000/tcp closed   ✅ (ВАЖНО!)
# 8080/tcp closed   ✅ (ВАЖНО!)
# 5432/tcp closed   ✅ (ВАЖНО!)
```

### Проверить security заголовки

```bash
curl -I https://ваш-домен.com
```

Должны быть заголовки:
```
Strict-Transport-Security: max-age=31536000
X-Content-Type-Options: nosniff
X-Frame-Options: SAMEORIGIN
```

### Тест Rate Limiting

```bash
# Отправить 150 запросов быстро
for i in {1..150}; do
  curl -s -o /dev/null -w "%{http_code}\n" https://ваш-домен.com/
done

# После 100-го должны быть ответы 429 (Too Many Requests)
```

---

## 🎉 Итоговый чеклист

После деплоя убедитесь:

- [ ] Контейнеры запущены (`docker compose ps`)
- [ ] Сайт открывается в браузере
- [ ] SSL сертификат получен (зеленый замок в браузере)
- [ ] Порты 3000/8080/5432 ЗАКРЫТЫ (nmap проверка)
- [ ] Security заголовки присутствуют (curl -I проверка)
- [ ] Rate limiting работает (тест 150 запросов)
- [ ] Логи без ошибок (`docker compose logs`)
- [ ] .env файлы НЕ закоммичены в GitHub
- [ ] Пароли БД и админа НЕ дефолтные

**Если все чекбоксы отмечены:** Приложение защищено! 🚀🔒

---

## 📞 Дополнительная помощь

- Полная инструкция: [CADDY-SETUP.md](CADDY-SETUP.md)
- WiFi настройка Orange Pi: [SSH-WIFI-SETUP.md](SSH-WIFI-SETUP.md)
- Настройка Orange Pi: [SETUP-ORANGEPI-ZERO3.html](SETUP-ORANGEPI-ZERO3.html)
- Документация Caddy: https://caddyserver.com/docs/
