# 🍊 Orange Pi Zero 3 - Итоговая инструкция по настройке

## Обнаруженные проблемы и решения

### ✅ Проблема 1: Frontend не может подключиться к Backend API
**Симптомы:** `GET http://95.31.238.60:3000/api/projects net::ERR_CONNECTION_REFUSED`

**Причина:** Frontend собран с `REACT_APP_API_URL=http://localhost:8080/api` вместо относительного пути `/api`

**Решение:** [deploy/FIX-API-URL.md](FIX-API-URL.md)

**Быстрое исправление:**
```bash
cd ~/portfolio
git pull origin main
chmod +x fix-frontend.sh
./fix-frontend.sh
```

---

### ✅ Проблема 2: WiFi соединение постоянно обрывается
**Симптомы:** WiFi периодически отключается, нужно переподключаться

**Причина:** WiFi адаптер переходит в режим энергосбережения (Power Management)

**Решение:** [deploy/WIFI-STABILITY-FIX.md](WIFI-STABILITY-FIX.md)

**Быстрое исправление:**
```bash
cd ~/portfolio
git pull origin main
chmod +x deploy/QUICK-WIFI-FIX.sh
sudo deploy/QUICK-WIFI-FIX.sh
sudo reboot
```

---

## 🚀 Полная процедура настройки Orange Pi с нуля

### 1. Базовая настройка системы
```bash
# Обновление системы
sudo apt update && sudo apt upgrade -y

# Установка необходимых пакетов
sudo apt install -y git curl wget htop nano network-manager
```

### 2. Установка Docker
```bash
# Установка Docker (если еще не установлен)
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Добавить пользователя в группу docker
sudo usermod -aG docker $USER

# Перелогиниться для применения изменений
exit
# SSH снова
```

### 3. Настройка WiFi с приоритетом
**Документация:** [deploy/SSH-WIFI-SETUP.md](SSH-WIFI-SETUP.md)

```bash
cd ~
# Создать скрипт из SSH-WIFI-SETUP.md
chmod +x wifi-priority-setup.sh
sudo ./wifi-priority-setup.sh
```

### 4. Исправление стабильности WiFi
```bash
cd ~
git clone https://github.com/timurtm72/portfolio.git
cd portfolio
chmod +x deploy/QUICK-WIFI-FIX.sh
sudo deploy/QUICK-WIFI-FIX.sh
sudo reboot
```

### 5. Запуск портфолио через Docker
```bash
cd ~/portfolio

# Создать .env файлы
cp backend/env.example backend/.env
cp frontend/env.example frontend/.env

# Запустить Docker Compose (займет 30-60 минут на первую сборку)
docker compose build
docker compose up -d

# Проверить статус
docker compose ps
docker compose logs -f
```

### 6. Проверка работы
```bash
# Проверить Docker контейнеры
docker compose ps

# Проверить логи
docker compose logs backend
docker compose logs frontend

# Проверить доступность API
curl http://localhost:8080/api/skills
curl http://localhost:3000/api/skills

# Открыть в браузере
# http://95.31.238.60:3000
```

---

## 📋 Чеклист после настройки

- [ ] Docker установлен и работает: `docker --version`
- [ ] WiFi подключен и стабилен: `iwconfig wlan0 | grep "Power Management"`
- [ ] WiFi приоритет выше Ethernet: `ip route show` (wlan0 metric 100)
- [ ] Power Management отключен: должно быть `Power Management:off`
- [ ] WiFi Watchdog запущен: `systemctl status wifi-watchdog` (опционально)
- [ ] Все Docker контейнеры работают: `docker compose ps`
- [ ] Backend healthy: `curl http://localhost:8080/api/actuator/health`
- [ ] Frontend отдает файлы: `curl http://localhost:3000`
- [ ] API доступен через frontend: `curl http://localhost:3000/api/skills`
- [ ] Сайт открывается в браузере: http://95.31.238.60:3000

---

## 🛠️ Полезные команды для управления

### Docker
```bash
# Статус контейнеров
docker compose ps

# Логи всех сервисов
docker compose logs -f

# Логи конкретного сервиса
docker compose logs -f backend
docker compose logs -f frontend
docker compose logs -f db

# Перезапуск сервиса
docker compose restart backend
docker compose restart frontend

# Остановка/запуск
docker compose stop
docker compose start

# Полный рестарт
docker compose down
docker compose up -d

# Пересборка после изменений
docker compose build --no-cache
docker compose up -d
```

### WiFi
```bash
# Статус WiFi
nmcli device status
iwconfig wlan0

# Проверка Power Management
iwconfig wlan0 | grep "Power Management"

# Переподключение WiFi
sudo nmcli connection down WiFi-1
sudo nmcli connection up WiFi-1

# Проверка маршрутов (WiFi должен быть первым с metric 100)
ip route show

# Мониторинг WiFi watchdog (если установлен)
sudo journalctl -u wifi-watchdog -f
```

### Система
```bash
# Загрузка CPU и памяти
htop

# Температура
cat /sys/class/thermal/thermal_zone0/temp | awk '{print $1/1000 "°C"}'

# Использование диска
df -h

# Логи системы
journalctl -xe
dmesg | tail -50
```

---

## 📁 Структура проекта на Orange Pi

```
/home/timur/portfolio/
├── backend/
│   ├── .env                      # DB_HOST=db, CORS_ALLOWED_ORIGINS=*
│   ├── Dockerfile                # Amazon Corretto для ARM64
│   └── ...
├── frontend/
│   ├── .env                      # REACT_APP_API_URL=/api
│   ├── Dockerfile                # Node.js + Nginx
│   └── ...
├── database/
│   └── schema.sql                # Автоматически применяется при старте
├── docker-compose.yml            # Оркестрация 3 сервисов
├── fix-frontend.sh               # Скрипт исправления API URL
├── check-status.sh               # Диагностика Docker
└── deploy/
    ├── FIX-API-URL.md            # Инструкция по API URL
    ├── WIFI-STABILITY-FIX.md     # Полная документация WiFi
    ├── QUICK-WIFI-FIX.sh         # Быстрое исправление WiFi
    ├── SSH-WIFI-SETUP.md         # Настройка WiFi приоритета
    └── README-ORANGEPI.md        # Этот файл
```

---

## 🔍 Troubleshooting

### Frontend не грузит данные
```bash
# Проверить логи frontend
docker compose logs frontend | tail -50

# Проверить Nginx конфигурацию внутри контейнера
docker compose exec frontend cat /etc/nginx/conf.d/default.conf

# Проверить доступность backend изнутри frontend
docker compose exec frontend wget -qO- http://backend:8080/api/skills
```

### Backend не запускается
```bash
# Проверить логи backend
docker compose logs backend | grep -i error

# Проверить подключение к БД
docker compose exec backend nc -zv db 5432

# Перезапустить backend
docker compose restart backend
```

### База данных не работает
```bash
# Проверить статус БД
docker compose ps db

# Логи БД
docker compose logs db

# Подключиться к БД
docker compose exec db psql -U portfolio_user -d portfolio_db

# Проверить таблицы
\dt
SELECT count(*) FROM projects;
```

### WiFi нестабилен
```bash
# Проверить Power Management
iwconfig wlan0 | grep "Power Management"

# Должно быть: Power Management:off
# Если нет - запустить:
sudo iwconfig wlan0 power off

# Проверить watchdog
sudo systemctl status wifi-watchdog

# Мониторинг сигнала
watch -n 1 'iwconfig wlan0 | grep -E "Quality|Signal"'
```

### Высокая загрузка или перегрев
```bash
# Температура (критично > 80°C)
watch -n 1 'cat /sys/class/thermal/thermal_zone0/temp | awk "{print \$1/1000 \"°C\"}"'

# Загрузка CPU
htop

# Ресурсы Docker
docker stats

# Если перегрев - добавить радиатор или вентилятор
```

---

## 🔄 Обновление кода с GitHub

```bash
cd ~/portfolio

# Остановить контейнеры
docker compose down

# Обновить код
git pull origin main

# Пересобрать (если изменился Dockerfile или код)
docker compose build

# Запустить
docker compose up -d

# Проверить логи
docker compose logs -f
```

---

## 🌐 Доступ к сайту

- **Локально:** http://localhost:3000
- **По локальной сети:** http://192.168.X.X:3000
- **По внешнему IP:** http://95.31.238.60:3000

**Backend API:**
- Локально: http://localhost:8080/api
- Через frontend: http://localhost:3000/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html

---

## 📞 Полезные ссылки

- **GitHub:** https://github.com/timurtm72/portfolio
- **Основная документация:** [CLAUDE.md](../CLAUDE.md)
- **Docker деплой:** [Основной раздел в CLAUDE.md](../CLAUDE.md#docker-деплой)
- **Orange Pi setup:** [deploy/SETUP-ORANGEPI-ZERO3.html](SETUP-ORANGEPI-ZERO3.html)

---

**© 2025 Orange Pi Zero 3 Portfolio Deployment**
**Проверено и работает** ✅
