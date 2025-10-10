# 🔧 Исправление нестабильного WiFi на Orange Pi Zero 3

## Проблема
WiFi соединение постоянно обрывается на Orange Pi Zero 3.

## Возможные причины
1. **Power Management** - WiFi адаптер переходит в режим энергосбережения
2. **Слабый сигнал** - недостаточный уровень сигнала WiFi
3. **Конфликт драйверов** - проблемы с драйвером WiFi модуля
4. **NetworkManager timeout** - слишком короткие таймауты
5. **Перегрев** - чип перегревается и сбрасывает соединение

---

## ⚡ БЫСТРОЕ РЕШЕНИЕ - Отключить энергосбережение WiFi

Скопируйте и выполните на Orange Pi:

```bash
# 1. Отключить power management для WiFi
sudo iwconfig wlan0 power off

# 2. Проверить статус
iwconfig wlan0 | grep "Power Management"

# 3. Сделать постоянным (создать systemd service)
sudo tee /etc/systemd/system/wifi-power-off.service > /dev/null << 'EOF'
[Unit]
Description=Disable WiFi Power Management
After=network.target

[Service]
Type=oneshot
ExecStart=/usr/sbin/iwconfig wlan0 power off
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
EOF

# 4. Включить и запустить сервис
sudo systemctl daemon-reload
sudo systemctl enable wifi-power-off.service
sudo systemctl start wifi-power-off.service

# 5. Проверить работу
sudo systemctl status wifi-power-off.service
```

---

## 🛠️ КОМПЛЕКСНОЕ РЕШЕНИЕ - Скрипт стабилизации

### Создайте скрипт на Orange Pi:

```bash
cd ~
cat > wifi-stability-fix.sh << 'EOF'
#!/bin/bash

echo "======================================"
echo "  WiFi Stability Fix для Orange Pi"
echo "======================================"
echo ""

if [ "$EUID" -ne 0 ]; then
    echo "Ошибка: Запустите с sudo"
    exit 1
fi

# 1. Отключить power management
echo "[1/6] Отключение Power Management для WiFi..."
iwconfig wlan0 power off
echo "✓ Power Management: OFF"

# 2. Создать systemd service для отключения PM при загрузке
echo "[2/6] Создание systemd service..."
cat > /etc/systemd/system/wifi-power-off.service << 'EOFSERVICE'
[Unit]
Description=Disable WiFi Power Management
After=network.target

[Service]
Type=oneshot
ExecStart=/usr/sbin/iwconfig wlan0 power off
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
EOFSERVICE

systemctl daemon-reload
systemctl enable wifi-power-off.service
systemctl start wifi-power-off.service
echo "✓ Systemd service создан и активирован"

# 3. Настроить NetworkManager для WiFi
echo "[3/6] Настройка NetworkManager..."
mkdir -p /etc/NetworkManager/conf.d

cat > /etc/NetworkManager/conf.d/wifi-stability.conf << 'EOFNM'
[connection]
wifi.powersave = 2

[device]
wifi.scan-rand-mac-address = no
wifi.backend = wpa_supplicant

[main]
dhcp = internal
EOFNM

echo "✓ NetworkManager сконфигурирован"

# 4. Увеличить таймауты WiFi
echo "[4/6] Настройка WiFi таймаутов..."
WIFI_CONN=$(nmcli -t -f NAME,TYPE connection show | grep wifi | head -1 | cut -d: -f1)

if [ -n "$WIFI_CONN" ]; then
    nmcli connection modify "$WIFI_CONN" connection.auth-retries 0
    nmcli connection modify "$WIFI_CONN" wifi.powersave 2
    nmcli connection modify "$WIFI_CONN" ipv4.dhcp-timeout 300
    echo "✓ Таймауты увеличены для '$WIFI_CONN'"
else
    echo "⚠ WiFi подключение не найдено, пропускаем"
fi

# 5. Отключить unnecessary services
echo "[5/6] Отключение ModemManager (может конфликтовать с WiFi)..."
systemctl disable ModemManager 2>/dev/null || true
systemctl stop ModemManager 2>/dev/null || true
echo "✓ ModemManager отключен"

# 6. Настроить автоматический мониторинг и переподключение
echo "[6/6] Создание watchdog для WiFi..."
cat > /usr/local/bin/wifi-watchdog.sh << 'EOFWATCHDOG'
#!/bin/bash
# WiFi Watchdog - автоматическое переподключение при обрыве

INTERFACE="wlan0"
PING_HOST="8.8.8.8"
MAX_FAILS=3
FAIL_COUNT=0

while true; do
    # Проверяем статус интерфейса
    if ! ip link show "$INTERFACE" | grep -q "state UP"; then
        echo "$(date): $INTERFACE DOWN, поднимаем..."
        ip link set "$INTERFACE" up
        sleep 5
    fi

    # Пингуем гугл
    if ping -I "$INTERFACE" -c 1 -W 3 "$PING_HOST" > /dev/null 2>&1; then
        FAIL_COUNT=0
    else
        FAIL_COUNT=$((FAIL_COUNT + 1))
        echo "$(date): Ping failed ($FAIL_COUNT/$MAX_FAILS)"

        if [ $FAIL_COUNT -ge $MAX_FAILS ]; then
            echo "$(date): Переподключение WiFi..."

            # Перезапуск WiFi
            nmcli radio wifi off
            sleep 2
            nmcli radio wifi on
            sleep 5

            # Переподключение к последней сети
            WIFI_CONN=$(nmcli -t -f NAME,TYPE connection show --active | grep wifi | head -1 | cut -d: -f1)
            if [ -n "$WIFI_CONN" ]; then
                nmcli connection down "$WIFI_CONN" || true
                sleep 2
                nmcli connection up "$WIFI_CONN"
            fi

            FAIL_COUNT=0
            sleep 30
        fi
    fi

    sleep 10
done
EOFWATCHDOG

chmod +x /usr/local/bin/wifi-watchdog.sh

# Создать systemd service для watchdog
cat > /etc/systemd/system/wifi-watchdog.service << 'EOFWATCHDOGSERVICE'
[Unit]
Description=WiFi Connection Watchdog
After=network-online.target
Wants=network-online.target

[Service]
Type=simple
ExecStart=/usr/local/bin/wifi-watchdog.sh
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOFWATCHDOGSERVICE

systemctl daemon-reload
systemctl enable wifi-watchdog.service
systemctl start wifi-watchdog.service
echo "✓ WiFi Watchdog активирован"

echo ""
echo "======================================"
echo "  Перезапуск NetworkManager..."
echo "======================================"
systemctl restart NetworkManager
sleep 5

echo ""
echo "======================================"
echo "  ПРОВЕРКА СТАТУСА"
echo "======================================"
echo ""

echo "Power Management:"
iwconfig wlan0 2>/dev/null | grep "Power Management" || echo "iwconfig недоступен"

echo ""
echo "WiFi статус:"
nmcli device status | grep wifi

echo ""
echo "Активные подключения:"
nmcli connection show --active | grep wifi

echo ""
echo "Сервисы:"
systemctl is-active wifi-power-off.service
systemctl is-active wifi-watchdog.service

echo ""
echo "======================================"
echo "  ✅ ГОТОВО!"
echo "======================================"
echo ""
echo "Что сделано:"
echo "  1. Отключен Power Management WiFi"
echo "  2. Создан systemd service для PM"
echo "  3. Настроен NetworkManager"
echo "  4. Увеличены таймауты WiFi"
echo "  5. Отключен ModemManager"
echo "  6. Запущен WiFi Watchdog"
echo ""
echo "Проверьте стабильность через 10-15 минут:"
echo "  journalctl -u wifi-watchdog -f"
echo ""
EOF

chmod +x wifi-stability-fix.sh
```

### Запустите скрипт:

```bash
sudo ./wifi-stability-fix.sh
```

---

## 🧪 ПРОВЕРКА РАБОТЫ

### 1. Проверить Power Management
```bash
iwconfig wlan0 | grep "Power Management"
# Должно быть: Power Management:off
```

### 2. Проверить сервисы
```bash
sudo systemctl status wifi-power-off.service
sudo systemctl status wifi-watchdog.service
```

### 3. Мониторинг watchdog
```bash
# Смотреть логи в реальном времени
sudo journalctl -u wifi-watchdog -f

# Последние 50 строк
sudo journalctl -u wifi-watchdog -n 50
```

### 4. Проверить стабильность сигнала
```bash
watch -n 1 'iwconfig wlan0 | grep -E "Link Quality|Signal level"'
```

### 5. Проверить переподключения
```bash
# Статистика NetworkManager
nmcli device wifi list
nmcli device status
```

---

## 🔍 ДИАГНОСТИКА ПРОБЛЕМ

### Проверить уровень сигнала WiFi
```bash
iwconfig wlan0 | grep -E "Signal level|Link Quality"
```

**Хороший сигнал:** Link Quality > 50/70, Signal level > -60 dBm
**Плохой сигнал:** Link Quality < 30/70, Signal level < -70 dBm

### Проверить ошибки WiFi драйвера
```bash
dmesg | grep -i "wifi\|wlan" | tail -20
journalctl -k | grep -i "wifi\|wlan" | tail -20
```

### Проверить загрузку системы
```bash
# Высокая температура может вызывать обрывы
cat /sys/class/thermal/thermal_zone*/temp

# Загрузка CPU
top -bn1 | head -5
```

### Тест стабильности соединения
```bash
# Непрерывный ping с логированием
ping -i 1 8.8.8.8 | while read line; do echo "$(date): $line"; done | tee wifi-ping-test.log
```

Запустите на 10-15 минут и проверьте логи на наличие пропущенных пакетов.

---

## 🛠️ ДОПОЛНИТЕЛЬНЫЕ РЕШЕНИЯ

### Если WiFi все еще нестабилен - переключите драйвер

```bash
# Проверить текущий драйвер
lsmod | grep 8189

# Если используется 8189es/8189fs, попробуйте обновить firmware
sudo apt update
sudo apt install -y linux-firmware firmware-realtek

# Перезагрузка
sudo reboot
```

### Если проблема в роутере - зафиксировать канал WiFi

На роутере:
1. Зайдите в настройки WiFi
2. Отключите автоматический выбор канала
3. Выберите фиксированный канал (1, 6 или 11 для 2.4GHz)
4. Отключите DFS для 5GHz

### Использовать статический IP вместо DHCP

```bash
# Для WiFi-1
sudo nmcli connection modify WiFi-1 ipv4.method manual
sudo nmcli connection modify WiFi-1 ipv4.addresses "192.168.1.100/24"
sudo nmcli connection modify WiFi-1 ipv4.gateway "192.168.1.1"
sudo nmcli connection modify WiFi-1 ipv4.dns "8.8.8.8,8.8.4.4"

sudo nmcli connection down WiFi-1
sudo nmcli connection up WiFi-1
```

---

## 📊 МОНИТОРИНГ СТАБИЛЬНОСТИ

### Создать скрипт мониторинга

```bash
cat > ~/wifi-monitor.sh << 'EOF'
#!/bin/bash
echo "=== WiFi Monitoring ==="
echo "Press Ctrl+C to stop"
echo ""

while true; do
    clear
    echo "$(date)"
    echo "===================="
    echo ""

    echo "Статус интерфейса:"
    ip link show wlan0
    echo ""

    echo "Качество сигнала:"
    iwconfig wlan0 2>/dev/null | grep -E "Quality|Signal"
    echo ""

    echo "Активное подключение:"
    nmcli connection show --active | grep wifi
    echo ""

    echo "Ping test:"
    ping -c 3 -W 2 8.8.8.8 | tail -2
    echo ""

    echo "Температура:"
    cat /sys/class/thermal/thermal_zone0/temp | awk '{print $1/1000 "°C"}'
    echo ""

    sleep 5
done
EOF

chmod +x ~/wifi-monitor.sh
```

Запуск:
```bash
~/wifi-monitor.sh
```

---

## 🎯 ЧЕКЛИСТ СТАБИЛЬНОСТИ

После применения всех исправлений проверьте:

- [ ] `iwconfig wlan0` показывает `Power Management:off`
- [ ] `systemctl status wifi-power-off.service` активен
- [ ] `systemctl status wifi-watchdog.service` запущен
- [ ] Непрерывный ping работает без потерь пакетов 15+ минут
- [ ] WiFi не переподключается без причины
- [ ] Уровень сигнала стабилен (Link Quality > 40/70)
- [ ] Docker контейнеры работают стабильно

---

## 🔑 КЛЮЧЕВЫЕ КОМАНДЫ

Отключить Power Management:
```bash
sudo iwconfig wlan0 power off
```

Перезапустить WiFi:
```bash
sudo nmcli radio wifi off && sleep 2 && sudo nmcli radio wifi on
```

Переподключить WiFi:
```bash
sudo nmcli connection down WiFi-1 && sudo nmcli connection up WiFi-1
```

Проверить логи watchdog:
```bash
sudo journalctl -u wifi-watchdog -f
```

---

## 🆘 ЕСЛИ НИЧЕГО НЕ ПОМОГЛО

1. **Используйте Ethernet** как основное подключение
2. **Переместите роутер ближе** к Orange Pi
3. **Используйте внешний USB WiFi адаптер** с лучшим чипсетом
4. **Обновите firmware Orange Pi** до последней версии

---

**© 2025 Orange Pi WiFi Stability Fix**
**Проверено на Orange Pi Zero 3** ✅
