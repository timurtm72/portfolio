# 📝 Пошаговая инструкция для деплоя на Orange Pi (для начинающих)

**Время выполнения:** ~5 минут подготовки + 30-60 минут автоматической сборки

---

## 🏗️ Архитектура безопасности

```
Интернет
   ↓
UFW Firewall (только 22, 80, 443)
   ↓
Caddy :80/:443 (reverse proxy + защита от ботов)
   ↓
┌──────────────────────────────────┐
│ Docker Network (внутренняя сеть) │
│                                  │
│  Frontend :3000 ←─┐              │
│                   │              │
│  Backend :8080  ←─┤              │
│                   │              │
│  PostgreSQL :5432 ←┘             │
└──────────────────────────────────┘

✅ Снаружи доступны: 80, 443
❌ Снаружи ЗАКРЫТЫ: 3000, 8080, 5432
```

**Все внутренние порты (3000, 8080, 5432) доступны только внутри Docker и защищены Caddy!**

---

## Шаг 1: Подключитесь к Orange Pi

**На вашем компьютере** откройте терминал (PowerShell или CMD) и введите:

```bash
ssh timur@95.31.238.60
```

Введите пароль когда попросит.

✅ **Результат:** Вы должны увидеть приглашение `timur@orangepizero3:~$`

---

## Шаг 2: Перейдите в папку проекта

```bash
cd ~/portfolio
```

✅ **Результат:** Приглашение изменится на `timur@orangepizero3:~/portfolio$`

---

## Шаг 3: Проверьте что код обновлён

```bash
git status
```

✅ **Должно показать:** `On branch main` и `Your branch is up to date with 'origin/main'`

---

## Шаг 4: Создайте файл backend/.env

```bash
nano backend/.env
```

**Откроется текстовый редактор.** Вставьте этот текст (изменив пароли!):

```bash
DB_HOST=db
DB_PORT=5432
DB_USER=portfolio_user
DB_PASSWORD=MySecurePassword123!@#

ADMIN_USERNAME=admin
ADMIN_PASSWORD=AdminSecurePassword456!@#

JWT_SECRET=ОСТАВЬТЕ_ПУСТЫМ_СЕЙЧАС_ЗАПОЛНИМ_В_ШАГЕ_5

CORS_ALLOWED_ORIGINS=http://95.31.238.60
```

**⚠️ ОБЯЗАТЕЛЬНО измените:**
- `MySecurePassword123!@#` → придумайте свой пароль БД (минимум 20 символов)
- `AdminSecurePassword456!@#` → придумайте пароль админа (минимум 20 символов)

**Сохраните файл:**
1. Нажмите `Ctrl + O` (буква O, не ноль)
2. Нажмите `Enter`
3. Нажмите `Ctrl + X`

✅ **Результат:** Файл сохранён, вернулись в командную строку

---

## Шаг 5: Сгенерируйте JWT секрет

```bash
openssl rand -base64 64
```

✅ **Результат:** Увидите длинную строку типа:
```
aB3dF5gH7jK9lM2nP4qR6sT8uV0wX1yZ3aB5cD7eF9gH1iJ3kL5mN7oP9qR1sT3uV5wX7yZ9
```

**Скопируйте эту строку** (выделите мышкой, правая кнопка → Copy)

---

## Шаг 6: Вставьте JWT секрет в backend/.env

```bash
nano backend/.env
```

**Найдите строку:**
```
JWT_SECRET=ОСТАВЬТЕ_ПУСТЫМ_СЕЙЧАС_ЗАПОЛНИМ_В_ШАГЕ_5
```

**Замените на** (вставьте скопированную строку):
```
JWT_SECRET=aB3dF5gH7jK9lM2nP4qR6sT8uV0wX1yZ3aB5cD7eF9gH1iJ3kL5mN7oP9qR1sT3uV5wX7yZ9
```

**Сохраните:**
1. `Ctrl + O`
2. `Enter`
3. `Ctrl + X`

✅ **Результат:** JWT секрет добавлен

---

## Шаг 7: Создайте файл frontend/.env

```bash
nano frontend/.env
```

**Вставьте эту строку:**
```bash
REACT_APP_API_URL=/api
```

**Сохраните:**
1. `Ctrl + O`
2. `Enter`
3. `Ctrl + X`

✅ **Результат:** Файл frontend/.env создан

---

## Шаг 8: Создайте Caddyfile (защита от ботов)

```bash
nano Caddyfile
```

**Вставьте этот конфиг:**

```
:80 {
    # Rate limiting - 100 запросов в минуту с одного IP
    rate_limit {
        zone dynamic {
            key {remote_host}
            events 100
            window 1m
        }
    }
    
    # Security headers (защита от XSS, clickjacking)
    header {
        X-Frame-Options "DENY"
        X-Content-Type-Options "nosniff"
        X-XSS-Protection "1; mode=block"
        Referrer-Policy "strict-origin-when-cross-origin"
        -Server
    }
    
    # Блокировка поисковых ботов и скраперов
    @bots {
        header User-Agent *bot*
        header User-Agent *crawler*
        header User-Agent *spider*
        header User-Agent *scraper*
    }
    respond @bots 403
    
    # Frontend (все запросы кроме /api)
    handle /* {
        reverse_proxy frontend:3000
    }
    
    # Backend API (запросы на /api/*)
    handle /api/* {
        reverse_proxy backend:8080
    }
}
```

**Что делает этот конфиг:**
- Слушает порт 80 (HTTP)
- Ограничивает 100 запросов/мин с одного IP
- Блокирует ботов по User-Agent
- Проксирует запросы на frontend:3000 и backend:8080
- Добавляет security заголовки

**Сохраните:**
1. `Ctrl + O`
2. `Enter`
3. `Ctrl + X`

✅ **Результат:** Caddyfile создан с защитой

---

## Шаг 9: Проверьте docker-compose.yml

**Убедитесь что порты НЕ публикуются напрямую:**

```bash
cat docker-compose.yml | grep -A 2 "ports:"
```

✅ **Правильно должно быть:**
```yaml
caddy:
  ports:
    - "80:80"
    - "443:443"

# Frontend и Backend БЕЗ ports, только expose:
frontend:
  expose:
    - "3000"

backend:
  expose:
    - "8080"

# DB вообще без портов наружу
```

❌ **НЕПРАВИЛЬНО если увидите:**
```yaml
frontend:
  ports:
    - "3000:3000"  # ← Так НЕЛЬЗЯ! Порт открыт наружу!
```

Если видите `ports:` у frontend/backend/db - **УДАЛИТЕ** их и замените на `expose:`

---

## Шаг 10: Проверьте что файлы созданы

```bash
ls -la backend/.env
ls -la frontend/.env
ls -la Caddyfile
ls -la docker-compose.yml
```

✅ **Должно показать все 4 файла**

Если чего-то не хватает - повторите соответствующие шаги.

---

## Шаг 11: Настройте firewall (защита портов)

**Скопируйте и вставьте все команды сразу:**

```bash
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow 22/tcp comment 'SSH'
sudo ufw allow 80/tcp comment 'HTTP'
sudo ufw allow 443/tcp comment 'HTTPS'
sudo ufw --force enable
```

Введите пароль если попросит.

✅ **Результат:** Увидите `Firewall is active and enabled on system startup`

**Проверьте:**
```bash
sudo ufw status verbose
```

✅ **Должно показать:**
```
Status: active
Logging: on (low)
Default: deny (incoming), allow (outgoing), disabled (routed)

To                         Action      From
--                         ------      ----
22/tcp                     ALLOW IN    Anywhere                   # SSH
80/tcp                     ALLOW IN    Anywhere                   # HTTP
443/tcp                    ALLOW IN    Anywhere                   # HTTPS
```

**Это значит:**
- ✅ Порт 22 (SSH) - открыт
- ✅ Порт 80 (HTTP) - открыт
- ✅ Порт 443 (HTTPS) - открыт
- ❌ Все остальные порты (3000, 8080, 5432) - ЗАКРЫТЫ

---

## Шаг 12: Запустите автоматический деплой

```bash
chmod +x deploy/caddy-deploy.sh
bash deploy/caddy-deploy.sh
```

⏳ **Ожидайте 30-60 минут!** Orange Pi медленный, это нормально.

**Что будет происходить:**
1. ✅ Проверка окружения (Docker, файлы)
2. ✅ Остановка старых контейнеров (если есть)
3. ⏳ **Сборка Docker образов** ← Самый долгий этап (30-60 мин)!
4. ✅ Запуск контейнеров
5. ✅ Проверка работоспособности

✅ **Результат успешного деплоя:**
```
════════════════════════════════════════════════════════════════
✓ Деплой завершен успешно! 🚀
════════════════════════════════════════════════════════════════

📍 Приложение доступно по адресу:
   http://95.31.238.60

🔒 Архитектура безопасности:
   Интернет → Caddy :80/:443 → Frontend :3000 → Backend :8080 → DB :5432
              (защита)          (внутри Docker)
```

---

## Шаг 13: Проверьте что сайт работает

**На вашем компьютере** откройте браузер и перейдите на:

```
http://95.31.238.60
```

✅ **Должен открыться ваш сайт-портфолио**

⚠️ **КРИТИЧЕСКИ ВАЖНО:** Без домена нет HTTPS - все данные передаются открытым текстом!
- ❌ **НЕ вводите реальные пароли**
- ❌ **НЕ передавайте конфиденциальные данные**
- ❌ **НЕ используйте для production с реальными пользователями**
- ✅ **Для production ОБЯЗАТЕЛЬНО нужен домен и HTTPS**

---

## Шаг 14: Проверьте безопасность портов (ВАЖНО!)

**На вашем компьютере** (НЕ на Orange Pi!) откройте терминал и введите:

```bash
nmap -p 22,80,443,3000,8080,5432 95.31.238.60
```

Если нет `nmap`, установите:
- **Windows:** [скачать](https://nmap.org/download.html)
- **Linux/Mac:** `sudo apt install nmap` или `brew install nmap`

✅ **ПРАВИЛЬНЫЙ результат (БЕЗОПАСНО):**
```
PORT     STATE
22/tcp   open      ← SSH (хорошо)
80/tcp   open      ← HTTP через Caddy (хорошо)
443/tcp  filtered  ← HTTPS (хорошо, пока не настроен)
3000/tcp closed    ← Frontend ЗАЩИЩЁН! ✅
8080/tcp closed    ← Backend ЗАЩИЩЁН! ✅
5432/tcp closed    ← Database ЗАЩИЩЕНА! ✅
```

❌ **НЕПРАВИЛЬНЫЙ результат (ОПАСНО!):**
```
PORT     STATE
3000/tcp open      ← ⚠️ ПРОБЛЕМА! Frontend доступен напрямую!
8080/tcp open      ← ⚠️ ПРОБЛЕМА! Backend доступен напрямую!
5432/tcp open      ← 🚨 КРИТИЧНО! База данных открыта в интернет!
```

**Если видите `open` для портов 3000/8080/5432:**
1. Проверьте `docker-compose.yml` - там должен быть `expose:` а НЕ `ports:`
2. Остановите контейнеры: `docker compose down`
3. Исправьте `docker-compose.yml`
4. Перезапустите: `docker compose up -d`
5. Проверьте снова: `nmap -p 3000,8080,5432 95.31.238.60`

---

## Шаг 15: Проверьте Docker контейнеры

```bash
# Проверить что контейнеры запущены
docker compose ps
```

✅ **Должны быть 4 контейнера:**
```
NAME                STATE
portfolio-caddy     running
portfolio-frontend  running
portfolio-backend   running
portfolio-db        running
```

```bash
# Проверить что НЕ работают от root
docker compose exec backend whoami
docker compose exec frontend whoami
```

✅ **Правильно:** НЕ должно быть `root`

---

## Шаг 16: Тест защиты от ботов

**На вашем компьютере:**

```bash
# Обычный запрос - должен работать (200 OK)
curl -I http://95.31.238.60

# Запрос от бота - должен блокироваться (403 Forbidden)
curl -I -A "Mozilla/5.0 (compatible; Googlebot/2.1)" http://95.31.238.60
```

✅ **Правильный результат:**
```
# Первый запрос:
HTTP/1.1 200 OK

# Второй запрос (бот):
HTTP/1.1 403 Forbidden
```

❌ **Если оба возвращают 200** - Caddy не блокирует ботов, проверьте Caddyfile!

---

## 🎉 Готово! Что защищено?

### ✅ Уровни защиты:

**Уровень 1: UFW Firewall**
- Блокирует все порты кроме 22, 80, 443
- Защита на уровне операционной системы

**Уровень 2: Caddy (Reverse Proxy)**
- Rate Limiting - максимум 100 запросов/мин
- Блокировка ботов по User-Agent
- Security Headers (XSS, Clickjacking)
- Скрывает внутренние порты

**Уровень 3: Docker Network**
- Frontend:3000, Backend:8080, DB:5432 изолированы
- Доступны только внутри Docker сети
- Недоступны напрямую из интернета

**Уровень 4: Backend защита**
- JWT аутентификация
- CORS настроен только для вашего IP
- Валидация входных данных

### 📊 Статус безопасности:

| Компонент | Порт | Доступ из интернета | Статус |
|-----------|------|---------------------|--------|
| SSH | 22 | ✅ Открыт | Нужен для управления |
| Caddy HTTP | 80 | ✅ Открыт | Точка входа с защитой |
| Caddy HTTPS | 443 | ⚠️ Не настроен | Нужен домен |
| Frontend | 3000 | ❌ Закрыт | Только через Caddy |
| Backend | 8080 | ❌ Закрыт | Только через Caddy |
| PostgreSQL | 5432 | ❌ Закрыт | Только для Backend |

**Сайт доступен по адресу:** `http://95.31.238.60`

---

## 🔄 Как обновить сайт в будущем?

**На вашем компьютере:**
```bash
# Внесите изменения в код
git add .
git commit -m "описание изменений"
git push origin main
```

**На Orange Pi:**
```bash
ssh timur@95.31.238.60
cd ~/portfolio
git pull
bash deploy/caddy-deploy.sh
```

---

## 🔍 Мониторинг атак

### Смотреть заблокированные запросы:

```bash
# Все 403 (боты) и 429 (rate limit)
docker compose logs caddy | grep -E "429|403"

# Топ-10 IP адресов атакующих
docker compose logs caddy | grep 403 | grep -oE '\b([0-9]{1,3}\.){3}[0-9]{1,3}\b' | sort | uniq -c | sort -nr | head -10

# Статистика за последний час
docker compose logs --since 1h caddy | grep -c "403"
docker compose logs --since 1h caddy | grep -c "429"

# Следить в реальном времени
docker compose logs -f caddy | grep -E "403|429"
```

### Проверка защиты:

```bash
# Тест rate limiting (должен вернуть 429 после ~100 запросов)
for i in {1..150}; do curl -s -o /dev/null -w "%{http_code}\n" http://95.31.238.60; sleep 0.1; done

# Тест блокировки ботов (должен вернуть 403)
curl -I -A "Mozilla/5.0 (compatible; Googlebot/2.1)" http://95.31.238.60

# Проверка что внутренние порты закрыты
nmap -p 3000,8080,5432 95.31.238.60
```

---

## 🛡️ Дополнительная защита (опционально)

### Fail2ban - автобан атакующих IP

Если видите много атак в логах:

```bash
# Установить fail2ban
sudo apt install fail2ban -y

# Создать конфиг
sudo nano /etc/fail2ban/jail.local
```

**Добавить:**
```ini
[caddy-ratelimit]
enabled = true
port = http,https
filter = caddy-ratelimit
logpath = /var/lib/docker/containers/*/*.log
maxretry = 5
findtime = 300
bantime = 3600
action = ufw
```

**Создать фильтр:**
```bash
sudo nano /etc/fail2ban/filter.d/caddy-ratelimit.conf
```

```ini
[Definition]
failregex = .*"<HOST>".*"(429|403)".*
ignoreregex =
```

**Запустить:**
```bash
sudo systemctl restart fail2ban
sudo systemctl enable fail2ban
sudo fail2ban-client status caddy-ratelimit
```

### Проверка fail2ban:

```bash
# Статус
sudo fail2ban-client status

# Заблокированные IP
sudo fail2ban-client status caddy-ratelimit

# Разблокировать IP
sudo fail2ban-client set caddy-ratelimit unbanip 123.45.67.89
```

---

## 🆘 Что делать если что-то пошло не так?

### Проблема: Сайт не открывается

```bash
# Проверить статус контейнеров
docker compose ps

# Все должны быть в состоянии "running"
# Если нет - смотрите логи:
docker compose logs -f caddy
docker compose logs -f frontend
docker compose logs -f backend
```

### Проблема: Порты 3000/8080/5432 показывают "open" в nmap

```bash
# 1. Проверить docker-compose.yml
cat docker-compose.yml | grep -A 5 "frontend:"
cat docker-compose.yml | grep -A 5 "backend:"

# 2. Должно быть expose: а не ports:
# Если там ports: - исправить на expose:

# 3. Перезапустить
docker compose down
docker compose up -d

# 4. Проверить снова
nmap -p 3000,8080,5432 95.31.238.60
```

### Проблема: Боты НЕ блокируются (возвращается 200 вместо 403)

```bash
# Проверить Caddyfile
cat Caddyfile | grep -A 5 "@bots"

# Должна быть секция:
# @bots {
#     header User-Agent *bot*
# }
# respond @bots 403

# Перезапустить Caddy
docker compose restart caddy

# Проверить логи
docker compose logs caddy | tail -20
```

### Проблема: Ошибка при сборке Docker

```bash
# Посмотреть полные логи
docker compose build 2>&1 | tee build.log

# Очистить кеш и пересобрать
docker compose down
docker system prune -a
docker compose build --no-cache
docker compose up -d
```

### Проблема: Порт 80 занят

```bash
# Найти процесс на порту 80
sudo lsof -i :80

# Если это Apache/Nginx - остановить
sudo systemctl stop apache2 nginx
sudo systemctl disable apache2 nginx

# Перезапустить Caddy
docker compose restart caddy
```

### Проблема: Docker команды не работают

```bash
# Проверить Docker
sudo systemctl status docker

# Запустить если остановлен
sudo systemctl start docker

# Добавить в группу docker
sudo usermod -aG docker $USER

# ВЫЙТИ и ВОЙТИ заново
exit
# Затем ssh timur@95.31.238.60 снова
```

### Проблема: Не хватает места

```bash
# Проверить место
df -h

# Очистить старые образы
docker system prune -a
docker volume prune

# Удалить логи
sudo journalctl --vacuum-time=3d
```

### Проблема: Слишком много атак

```bash
# Заблокировать конкретный IP
sudo ufw deny from 123.45.67.89

# Посмотреть топ атакующих
docker compose logs caddy | grep 403 | grep -oE '\b([0-9]{1,3}\.){3}[0-9]{1,3}\b' | sort | uniq -c | sort -nr | head -20

# Заблокировать диапазон IP
sudo ufw deny from 123.45.0.0/16

# Посмотреть правила UFW
sudo ufw status numbered

# Удалить правило
sudo ufw delete [номер]
```

---

## 📚 Полезные команды

### Управление контейнерами

```bash
# Статус всех контейнеров
docker compose ps

# Логи в реальном времени
docker compose logs -f

# Логи конкретного сервиса
docker compose logs -f caddy
docker compose logs -f frontend
docker compose logs -f backend
docker compose logs -f db

# Последние 100 строк
docker compose logs --tail=100 caddy

# Перезапуск
docker compose restart

# Перезапуск одного сервиса
docker compose restart caddy

# Остановка всех
docker compose down

# Запуск
docker compose up -d

# Полная пересборка
docker compose down
docker compose build --no-cache
docker compose up -d

# Посмотреть использование ресурсов
docker stats
```

### Проверка системы

```bash
# Использование диска
df -h

# Использование памяти
free -h

# Открытые порты
sudo ss -tulpn | grep LISTEN

# Статус firewall
sudo ufw status verbose

# Проверка публичного IP
curl ifconfig.me

# Проверка всех сетевых подключений
sudo netstat -tunlp

# Проверка Docker сетей
docker network ls
docker network inspect portfolio_default
```

### Работа с .env файлами

```bash
# ⚠️ НЕ используйте cat для .env - там пароли!

# Редактирование
nano backend/.env
nano frontend/.env

# Проверка существования
ls -la backend/.env frontend/.env

# Права доступа
chmod 644 backend/.env
chmod 644 frontend/.env

# Резервная копия
cp backend/.env backend/.env.backup.$(date +%Y%m%d)
cp frontend/.env frontend/.env.backup.$(date +%Y%m%d)

# Восстановление
cp backend/.env.backup.20250101 backend/.env
```

### Git команды

```bash
# Статус
git status

# Обновление кода
git pull

# Отменить локальные изменения
git reset --hard origin/main

# История
git log --oneline -10

# Текущая ветка
git branch

# Посмотреть изменения
git diff

# Посмотреть последний коммит
git show
```

### Диагностика сети

```bash
# Ping сервера
ping -c 4 95.31.238.60

# Traceroute
traceroute 95.31.238.60

# Проверка портов изнутри
docker compose exec caddy wget -O- http://frontend:3000
docker compose exec caddy wget -O- http://backend:8080/api/health

# DNS проверка
nslookup 95.31.238.60
```

---

## 🔐 Чеклист безопасности

### Перед деплоем:

- [ ] Сгенерирован сильный JWT_SECRET (64+ символа)
- [ ] Сменены пароли DB_PASSWORD (20+ символов)
- [ ] Сменены пароли ADMIN_PASSWORD (20+ символов)
- [ ] CORS настроен только на ваш IP (без localhost)
- [ ] Файлы .env НЕ добавлены в Git
- [ ] Caddyfile создан с защитой от ботов
- [ ] docker-compose.yml использует expose а не ports для внутренних сервисов

### После деплоя:

- [ ] UFW firewall активен (`sudo ufw status`)
- [ ] Порты 3000/8080/5432 закрыты (`nmap -p 3000,8080,5432 95.31.238.60`)
- [ ] Caddy блокирует ботов (тест с curl -A "bot")
- [ ] Rate limiting работает (тест со 150 запросами)
- [ ] Контейнеры не работают от root (`docker compose exec backend whoami`)
- [ ] Security headers присутствуют (`curl -I http://95.31.238.60`)
- [ ] Сайт открывается в браузере
- [ ] Создана резервная копия .env файлов

### Регулярное обслуживание:

- [ ] Обновление системы: `sudo apt update && sudo apt upgrade`
- [ ] Обновление Docker образов: `docker compose pull`
- [ ] Проверка логов на атаки: `docker compose logs caddy | grep 403`
- [ ] Мониторинг места на диске: `df -h`
- [ ] Резервное копирование БД: `docker compose exec db pg_dump`
- [ ] Проверка UFW правил: `sudo ufw status`

---

## 💡 Советы по оптимизации

### Ускорение повторных деплоев

После первой сборки последующие будут быстрее, если не менять:
- `package.json` (frontend)
- `pom.xml` (backend)  
- Dockerfile'ы

### Мониторинг в реальном времени

```bash
# Открыть несколько терминалов и в каждом:
docker compose logs -f caddy      # Терминал 1
docker compose logs -f frontend   # Терминал 2
docker compose logs -f backend    # Терминал 3
docker stats                      # Терминал 4
```

### Быстрая проверка работоспособности

```bash
# Один-лайнер
docker compose ps && curl -I http://localhost && sudo ufw status | grep Status
```

### Автодеплой при git push

```bash
cd ~/portfolio
nano .git/hooks/post-merge
```

```bash
#!/bin/bash
cd ~/portfolio
bash deploy/caddy-deploy.sh > deploy-$(date +%Y%m%d-%H%M%S).log 2>&1
```

```bash
chmod +x .git/hooks/post-merge
```

Теперь после `git pull` будет автоматический деплой с логированием!

---

## 📞 Дополнительная помощь

Если что-то не работает:

### Шаг 1: Читайте логи

90% проблем видно в логах:

```bash
# Все логи сразу
docker compose logs

# Конкретный сервис
docker compose logs caddy | tail -50

# В реальном времени
docker compose logs -f
```

### Шаг 2: Проверьте основы

```bash
# Docker работает?
sudo systemctl status docker

# Файлы существуют?
ls -la backend/.env frontend/.env Caddyfile docker-compose.yml

# Порт 80 свободен?
sudo lsof -i :80

# Firewall настроен?
sudo ufw status verbose

# Контейнеры запущены?
docker compose ps

# Внутренние порты закрыты?
nmap -p 3000,8080,5432 95.31.238.60
```

### Шаг 3: Поиск в Google

Скопируйте текст ошибки из логов и ищите в Google/Stack Overflow

### Шаг 4: Обратитесь за помощью

Укажите:
- На каком шаге возникла проблема
- Что показывает терминал (текст или скриншот)
- Вывод `docker compose logs`
- Вывод `sudo ufw status`
- Вывод `nmap -p 80,3000,8080,5432 95.31.238.60`

---

## 📚 Дополнительная документация

- [DEPLOY-WITHOUT-DOMAIN.md](DEPLOY-WITHOUT-DOMAIN.md) - деплой без домена (подробно)
- [ARCHITECTURE.md](../ARCHITECTURE.md) - схема безопасности
- [CADDY-SETUP.md](CADDY-SETUP.md) - настройка Caddy
- [CLAUDE.md](../CLAUDE.md) - полная документация проекта

---

## 🎯 Итоговая схема безопасности

```
┌─────────────────────────────────────────────────────────────┐
│                         ИНТЕРНЕТ                             │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                      UFW FIREWALL                            │
│  Правила:                                                    │
│    ✅ ALLOW 22/tcp  (SSH)                                    │
│    ✅ ALLOW 80/tcp  (HTTP)                                   │
│    ✅ ALLOW 443/tcp (HTTPS)                                  │
│    ❌ DENY  *       (все остальное)                          │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                       CADDY :80/:443                         │
│  Защита:                                                     │
│    • Rate Limiting (100 req/min)                             │
│    • Блокировка ботов (User-Agent)                           │
│    • Security Headers                                        │
│    • Reverse Proxy                                           │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│              DOCKER INTERNAL NETWORK                         │
│  (порты недоступны из интернета)                             │
│                                                              │
│  ┌──────────────────┐                                        │
│  │ Frontend :3000   │  ← React приложение                    │
│  └──────────────────┘                                        │
│           ↓                                                  │
│  ┌──────────────────┐                                        │
│  │ Backend :8080    │  ← Spring Boot API                     │
│  └──────────────────┘                                        │
│           ↓                                                  │
│  ┌──────────────────┐                                        │
│  │ PostgreSQL :5432 │  ← База данных                         │
│  └──────────────────┘                                        │
└─────────────────────────────────────────────────────────────┘
```

**Ключевые моменты:**
1. ✅ Только Caddy слушает порты 80/443 снаружи
2. ✅ Frontend/Backend/DB изолированы внутри Docker
3. ✅ Все запросы проходят через Caddy (защита)
4. ✅ UFW блокирует прямой доступ к внутренним портам
5. ❌ Без домена нет HTTPS - не для production!

---

**Удачи с деплоем! 🚀**

_Помните: архитектура защищена, но без HTTPS не используйте для реальных данных!_