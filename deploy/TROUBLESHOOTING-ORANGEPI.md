# Диагностика проблем с портфолио на Orange Pi

## Проблема: Сайт не открывается в браузере

### Шаг 1: Подключитесь к Orange Pi через SSH

```bash
# Замените <IP> на IP адрес вашей платы
ssh orangepi@<IP>
# Или если настроили другого пользователя
ssh root@<IP>
```

**Как узнать IP адрес платы:**
- Если подключена к роутеру - посмотрите в админке роутера (обычно 192.168.1.X)
- Подключите монитор к плате и выполните: `ip addr show`
- Используйте сканер сети: `nmap -sn 192.168.1.0/24`

### Шаг 2: Проверьте статус Docker контейнеров

```bash
cd ~/portfolio  # или /opt/portfolio - где установлен проект

# Проверка статуса всех контейнеров
docker compose ps

# Должны быть запущены 3 контейнера:
# - portfolio-backend (Up, healthy)
# - portfolio-frontend (Up, healthy)
# - portfolio-db (Up, healthy)
```

**Что означают статусы:**
- `Up` - контейнер запущен
- `Exit 0` или `Exit 1` - контейнер упал, смотрите логи
- `Restarting` - контейнер постоянно перезапускается (проблема)

### Шаг 3: Проверьте логи контейнеров

```bash
# Логи всех контейнеров (последние 50 строк)
docker compose logs --tail=50

# Логи конкретного контейнера
docker compose logs -f backend    # Backend (Spring Boot)
docker compose logs -f frontend   # Frontend (Nginx)
docker compose logs -f db         # PostgreSQL
```

**Ищите в логах:**
- ❌ `ERROR` - ошибки приложения
- ❌ `Exception` - исключения Java
- ❌ `Connection refused` - проблемы с подключением к БД
- ❌ `Out of memory` - нехватка RAM
- ✅ `Started BackendApplication` - backend запустился
- ✅ `Database initialization completed` - БД инициализирована

### Шаг 4: Проверьте доступность портов изнутри платы

```bash
# Проверка портов
netstat -tulpn | grep -E ':(3000|8080|5432)'

# Или через ss
ss -tulpn | grep -E ':(3000|8080|5432)'

# Должны быть открыты:
# - 0.0.0.0:3000 - Frontend (Nginx)
# - 127.0.0.1:8080 - Backend (Spring Boot)
# - 127.0.0.1:5432 - PostgreSQL
```

### Шаг 5: Проверьте доступность сайта изнутри платы

```bash
# Проверка Frontend
curl -I http://localhost:3000
# Ожидается: HTTP/1.1 200 OK

# Проверка Backend API
curl http://localhost:8080/api/skills
# Ожидается: JSON с навыками

# Проверка базы данных
docker compose exec db psql -U portfolio_user -d portfolio_db -c "SELECT COUNT(*) FROM projects;"
# Ожидается: количество проектов
```

### Шаг 6: Проверьте сетевое подключение платы

```bash
# IP адрес платы
ip addr show

# Проверка WiFi соединения
nmcli connection show --active

# Проверка маршрутизации
ip route show

# Пинг роутера
ping -c 4 192.168.1.1  # Замените на IP вашего роутера

# Пинг из интернета
ping -c 4 8.8.8.8
```

### Шаг 7: Проверьте firewall (если настроен)

```bash
# Проверка UFW
sudo ufw status

# Если firewall блокирует - откройте порт 3000
sudo ufw allow 3000/tcp
```

---

## Частые проблемы и решения

### 1. Контейнеры не запущены (Exit 0/1)

**Причина:** Ошибка при запуске приложения

**Решение:**
```bash
# Посмотрите логи
docker compose logs backend
docker compose logs frontend
docker compose logs db

# Перезапустите контейнеры
docker compose down
docker compose up -d
```

### 2. Backend не может подключиться к PostgreSQL

**Ошибка в логах:**
```
Connection refused: connect
PSQLException: Connection to localhost:5432 refused
```

**Решение:**
```bash
# Проверьте что PostgreSQL запущен
docker compose ps db

# Перезапустите БД
docker compose restart db

# Проверьте логи БД
docker compose logs db

# Если БД не стартует - пересоздайте volume
docker compose down -v  # ⚠️ УДАЛИТ ВСЕ ДАННЫЕ!
docker compose up -d
```

### 3. Нехватка памяти (Out of Memory)

**Признаки:**
- Контейнеры постоянно перезапускаются
- В логах: `Killed`, `Out of memory`
- Система тормозит

**Решение:**
```bash
# Проверка использования памяти
free -h
docker stats

# Увеличьте swap (если <2GB RAM)
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile

# Добавьте в /etc/fstab для автозагрузки
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
```

### 4. Порт 3000 занят другим процессом

**Решение:**
```bash
# Найдите процесс на порту 3000
sudo lsof -i :3000

# Или
sudo netstat -tulpn | grep :3000

# Убейте процесс
sudo kill -9 <PID>

# Перезапустите контейнеры
docker compose restart frontend
```

### 5. WiFi отключился или плохой сигнал

**Решение:**
```bash
# Проверьте WiFi соединение
nmcli connection show --active

# Переподключитесь
sudo nmcli connection down <WIFI_NAME>
sudo nmcli connection up <WIFI_NAME>

# Проверьте силу сигнала
iwconfig wlan0

# Если сигнал слабый - используйте Ethernet
```

### 6. Docker образы не собрались для ARM64

**Ошибка:**
```
no match for platform in manifest: not found
```

**Решение:**
```bash
# Убедитесь что используете правильные Dockerfile
# Backend: amazoncorretto:17-alpine (НЕ eclipse-temurin!)
# Frontend: node:18 (НЕ node:18-alpine!)

# Пересоберите образы
docker compose build --no-cache
docker compose up -d
```

### 7. Frontend показывает "Cannot connect to backend"

**Причина:** Backend не запустился или недоступен

**Решение:**
```bash
# Проверьте что backend запущен
docker compose logs backend | grep "Started BackendApplication"

# Проверьте healthcheck
docker compose ps backend

# Проверьте переменные окружения
docker compose exec backend env | grep DB_

# Перезапустите backend
docker compose restart backend
```

---

## Полная переустановка (Last Resort)

Если ничего не помогает:

```bash
# 1. Остановите и удалите все контейнеры
docker compose down -v  # ⚠️ УДАЛИТ ВСЕ ДАННЫЕ БД!

# 2. Удалите старые образы
docker image prune -a

# 3. Пересоберите с нуля
docker compose build --no-cache

# 4. Запустите
docker compose up -d

# 5. Следите за логами
docker compose logs -f
```

---

## Проверка доступности с другого компьютера

После того как на самой плате всё работает:

```bash
# С вашего компьютера (в той же сети)
# Замените <ORANGEPI_IP> на IP платы
curl http://<ORANGEPI_IP>:3000

# Или откройте в браузере
http://<ORANGEPI_IP>:3000
```

**Если не открывается:**
1. Проверьте firewall на плате
2. Проверьте что плата и компьютер в одной сети
3. Проверьте роутер - возможно блокирует

---

## Автоматический запуск при загрузке Orange Pi

Если контейнеры не запускаются автоматически при включении платы:

```bash
# Проверьте что Docker запускается автоматически
sudo systemctl enable docker
sudo systemctl start docker

# Добавьте restart policy в docker-compose.yml
# (уже должно быть настроено: restart: unless-stopped)

# Или создайте systemd unit
sudo nano /etc/systemd/system/portfolio-docker.service
```

Содержимое `portfolio-docker.service`:
```ini
[Unit]
Description=Portfolio Docker Compose
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=/home/orangepi/portfolio
ExecStart=/usr/bin/docker compose up -d
ExecStop=/usr/bin/docker compose down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
```

Активация:
```bash
sudo systemctl daemon-reload
sudo systemctl enable portfolio-docker.service
sudo systemctl start portfolio-docker.service
```

---

## Контакты для помощи

Если проблема не решается - соберите диагностическую информацию:

```bash
# Сохраните вывод в файл
{
  echo "=== Docker PS ==="
  docker compose ps
  echo ""
  echo "=== Docker Logs Backend ==="
  docker compose logs --tail=100 backend
  echo ""
  echo "=== Docker Logs Frontend ==="
  docker compose logs --tail=100 frontend
  echo ""
  echo "=== Docker Logs DB ==="
  docker compose logs --tail=100 db
  echo ""
  echo "=== Ports ==="
  netstat -tulpn | grep -E ':(3000|8080|5432)'
  echo ""
  echo "=== Network ==="
  ip addr show
  ip route show
  echo ""
  echo "=== Memory ==="
  free -h
  echo ""
  echo "=== Docker Stats ==="
  docker stats --no-stream
} > ~/portfolio-diagnostics.txt

# Отправьте файл ~/portfolio-diagnostics.txt
```
