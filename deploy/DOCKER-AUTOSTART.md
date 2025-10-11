# Автозапуск Docker проекта на Orange Pi после перезагрузки

## Проблема

Docker контейнеры не запускаются автоматически после перезагрузки Orange Pi, даже если в `docker-compose.yml` установлено `restart: unless-stopped`.

## Причина

Docker Compose не запускается автоматически при загрузке системы. Политика `restart: unless-stopped` работает только для уже запущенных контейнеров при перезапуске Docker daemon, но не при загрузке системы.

## Решение: systemd service

### Шаг 1: Установка systemd unit файла

Подключитесь к Orange Pi по SSH и выполните:

```bash
# Скопировать файл сервиса
sudo cp /root/portfolio/deploy/portfolio-docker.service /etc/systemd/system/

# Установить правильные права доступа
sudo chmod 644 /etc/systemd/system/portfolio-docker.service
```

### Шаг 2: Активация автозапуска

```bash
# Перезагрузить конфигурацию systemd
sudo systemctl daemon-reload

# Включить автозапуск
sudo systemctl enable portfolio-docker.service

# Запустить сервис сейчас (для проверки)
sudo systemctl start portfolio-docker.service
```

### Шаг 3: Проверка статуса

```bash
# Проверить статус сервиса
sudo systemctl status portfolio-docker.service

# Проверить логи
sudo journalctl -u portfolio-docker.service -f

# Проверить запущенные контейнеры
docker compose ps
```

Ожидаемый вывод:
```
● portfolio-docker.service - Portfolio Docker Compose Application
     Loaded: loaded (/etc/systemd/system/portfolio-docker.service; enabled)
     Active: active (exited) since ...
```

### Шаг 4: Тестирование перезагрузки

```bash
# Перезагрузить Orange Pi
sudo reboot

# После перезагрузки (через SSH)
docker compose ps
```

Все три контейнера должны быть в статусе `Up`:
- `portfolio-db`
- `portfolio-backend`
- `portfolio-frontend`

## Управление сервисом

### Проверка статуса автозапуска
```bash
sudo systemctl is-enabled portfolio-docker.service
# Должно вывести: enabled
```

### Остановка автозапуска (если нужно)
```bash
sudo systemctl disable portfolio-docker.service
```

### Перезапуск сервиса
```bash
sudo systemctl restart portfolio-docker.service
```

### Просмотр логов
```bash
# Все логи
sudo journalctl -u portfolio-docker.service

# Последние 100 строк
sudo journalctl -u portfolio-docker.service -n 100

# В реальном времени
sudo journalctl -u portfolio-docker.service -f
```

## Как работает systemd unit

Файл `/etc/systemd/system/portfolio-docker.service`:

```ini
[Unit]
Description=Portfolio Docker Compose Application
Requires=docker.service           # Требует запущенный Docker
After=docker.service network-online.target  # Запускается после Docker и сети
Wants=network-online.target       # Ждет полной инициализации сети

[Service]
Type=oneshot                      # Одноразовая команда
RemainAfterExit=yes              # Считать активным после выполнения
WorkingDirectory=/root/portfolio  # Директория проекта
ExecStartPre=/usr/bin/docker compose down  # Очистка перед запуском
ExecStart=/usr/bin/docker compose up -d    # Запуск в фоне
ExecStop=/usr/bin/docker compose down      # Остановка
TimeoutStartSec=0                # Без таймаута

# Перезапуск при ошибках
Restart=on-failure
RestartSec=10s

[Install]
WantedBy=multi-user.target       # Автозапуск при обычной загрузке
```

### Ключевые моменты:

1. **`Requires=docker.service`** - systemd гарантирует, что Docker запущен
2. **`After=network-online.target`** - ждет полной инициализации сети (важно для WiFi!)
3. **`WorkingDirectory=/root/portfolio`** - Docker Compose работает в корректной директории
4. **`ExecStartPre`** - очищает старые контейнеры перед запуском
5. **`Restart=on-failure`** - автоматический перезапуск при ошибках

## Альтернативный способ: Docker Compose restart policy

Если systemd сервис не нужен, можно использовать только `restart: always` в `docker-compose.yml`:

```yaml
services:
  db:
    restart: always  # Вместо unless-stopped
  backend:
    restart: always
  frontend:
    restart: always
```

**Разница между политиками:**
- `restart: always` - всегда перезапускать, даже если контейнер был остановлен вручную
- `restart: unless-stopped` - перезапускать, кроме случаев ручной остановки

⚠️ **Важно:** Политика `restart` работает только если Docker daemon запущен. При загрузке системы нужно вручную запустить `docker compose up -d` ИЛИ использовать systemd сервис.

## Рекомендация

**Используйте systemd сервис** - это более надежный способ для автозапуска на Orange Pi, особенно с учетом:
- Ожидания инициализации WiFi
- Автоматического перезапуска при сбоях
- Централизованного логирования через journalctl
- Зависимостей от других системных сервисов

## Отладка проблем

### Контейнеры не запускаются после перезагрузки

```bash
# Проверить статус сервиса
sudo systemctl status portfolio-docker.service

# Проверить логи
sudo journalctl -u portfolio-docker.service -n 100 --no-pager

# Проверить Docker daemon
sudo systemctl status docker

# Проверить сеть
ip route show
ping -c 3 google.com
```

### Сервис в состоянии failed

```bash
# Посмотреть подробные логи
sudo journalctl -u portfolio-docker.service -xe

# Запустить вручную для диагностики
cd /root/portfolio
docker compose up -d

# Проверить docker-compose.yml
docker compose config
```

### Долгий запуск после перезагрузки

Это нормально для Orange Pi Zero 3 (особенно при сборке образов):
- Инициализация WiFi: ~10-30 секунд
- Запуск Docker: ~5-10 секунд
- Старт контейнеров: ~30-60 секунд
- Healthcheck backend: ~60-90 секунд

**Общее время:** 2-3 минуты до полной доступности приложения.

## Проверка автозапуска

После настройки systemd сервиса:

```bash
# 1. Проверить что сервис включен
sudo systemctl is-enabled portfolio-docker.service

# 2. Проверить статус
sudo systemctl status portfolio-docker.service

# 3. Перезагрузить Orange Pi
sudo reboot

# 4. После перезагрузки подключиться по SSH и проверить
docker compose ps

# Ожидаемый результат:
# NAME                    STATUS    PORTS
# portfolio-backend       Up        0.0.0.0:8080->8080/tcp
# portfolio-db            Up        5432/tcp
# portfolio-frontend      Up        0.0.0.0:3000->80/tcp
```

✅ Если все три контейнера в статусе `Up` - автозапуск работает корректно!

## Интеграция с WiFi автоподключением

Если используете WiFi приоритет из [SSH-WIFI-SETUP.md](SSH-WIFI-SETUP.md), systemd сервис автоматически дождется инициализации сети благодаря:

```ini
After=network-online.target
Wants=network-online.target
```

Это гарантирует, что Docker Compose запустится только после того, как Orange Pi подключится к WiFi (или Ethernet).
