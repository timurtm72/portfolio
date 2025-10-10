# 🔥 Настройка WiFi с ПРИОРИТЕТОМ над Ethernet

## Orange Pi - WiFi всегда в приоритете, даже с подключенным кабелем

---

## ⚡ БЫСТРОЕ ИСПРАВЛЕНИЕ (если WiFi уже настроен)

Если WiFi уже настроен, но интернет идет через Ethernet - выполните эти команды **по одной**:

```bash
sudo nmcli connection modify "Wired connection 1" connection.autoconnect-priority -10
sudo nmcli connection modify "Wired connection 1" ipv4.route-metric 1000
sudo nmcli connection modify WiFi-1 connection.autoconnect-priority 100
sudo nmcli connection modify WiFi-1 ipv4.route-metric 100
sudo nmcli connection modify WiFi-2 connection.autoconnect-priority 90
sudo nmcli connection modify WiFi-2 ipv4.route-metric 100
sudo nmcli connection down "Wired connection 1"
sudo nmcli connection up "Wired connection 1"
sudo nmcli connection down WiFi-1
sudo nmcli connection up WiFi-1
```

Проверка:

```bash
ip route show
ping -c 4 google.com
```

✅ **Готово!** Теперь интернет идет через WiFi.

---

## 📋 ПОЛНАЯ НАСТРОЙКА С НУЛЯ - СКРИПТ

### Шаг 1: Перейдите в домашнюю папку

```bash
cd ~
```

### Шаг 2: Создайте скрипт

Скопируйте **ВСЁ** от `cat` до `EOF` и вставьте в PuTTY:

```bash
cat > wifi-priority-setup.sh << 'EOF'
#!/bin/bash
echo "========================================="
echo "  WiFi ПРИОРИТЕТ над Ethernet"
echo "  (autoconnect-priority + route-metric)"
echo "========================================="
echo ""

if [ "$EUID" -ne 0 ]; then
    echo "Ошибка: Запустите с sudo"
    exit 1
fi

if ! command -v nmcli &> /dev/null; then
    echo "Установка NetworkManager..."
    apt-get update
    apt-get install -y network-manager
    systemctl enable NetworkManager
    systemctl start NetworkManager
    sleep 3
fi

if ! nmcli device status | grep -q "wifi"; then
    echo "Ошибка: WiFi адаптер не найден!"
    exit 1
fi

echo "=== ПЕРВАЯ WiFi СЕТЬ ==="
while true; do
    read -p "SSID первой сети: " SSID1
    if [ -n "$SSID1" ]; then
        break
    fi
    echo "SSID не может быть пустым!"
done

while true; do
    read -sp "Пароль первой сети: " PASS1
    echo ""
    if [ ${#PASS1} -ge 8 ]; then
        break
    fi
    echo "Пароль минимум 8 символов!"
done

echo ""
echo "=== ВТОРАЯ WiFi СЕТЬ ==="
while true; do
    read -p "SSID второй сети: " SSID2
    if [ -n "$SSID2" ]; then
        break
    fi
    echo "SSID не может быть пустым!"
done

while true; do
    read -sp "Пароль второй сети: " PASS2
    echo ""
    if [ ${#PASS2} -ge 8 ]; then
        break
    fi
    echo "Пароль минимум 8 символов!"
done

echo ""
echo "Какая WiFi сеть важнее если обе доступны?"
echo "1) $SSID1"
echo "2) $SSID2"
while true; do
    read -p "Выберите (1 или 2): " CHOICE
    if [ "$CHOICE" == "1" ] || [ "$CHOICE" == "2" ]; then
        break
    fi
    echo "Введите 1 или 2"
done

if [ "$CHOICE" == "1" ]; then
    PRIORITY1=100
    PRIORITY2=90
else
    PRIORITY1=90
    PRIORITY2=100
fi

echo ""
echo "Удаление старых настроек..."
nmcli connection delete "WiFi-1" 2>/dev/null || true
nmcli connection delete "WiFi-2" 2>/dev/null || true

echo ""
echo "Создание WiFi-1: $SSID1"
if nmcli connection add \
    type wifi \
    con-name "WiFi-1" \
    ifname wlan0 \
    ssid "$SSID1" \
    wifi-sec.key-mgmt wpa-psk \
    wifi-sec.psk "$PASS1" \
    connection.autoconnect yes \
    connection.autoconnect-priority $PRIORITY1 \
    ipv4.route-metric 100; then
    echo "WiFi-1 создан (приоритет: $PRIORITY1, метрика: 100)"
else
    echo "Ошибка создания WiFi-1"
    exit 1
fi

echo ""
echo "Создание WiFi-2: $SSID2"
if nmcli connection add \
    type wifi \
    con-name "WiFi-2" \
    ifname wlan0 \
    ssid "$SSID2" \
    wifi-sec.key-mgmt wpa-psk \
    wifi-sec.psk "$PASS2" \
    connection.autoconnect yes \
    connection.autoconnect-priority $PRIORITY2 \
    ipv4.route-metric 100; then
    echo "WiFi-2 создан (приоритет: $PRIORITY2, метрика: 100)"
else
    echo "Ошибка создания WiFi-2"
    exit 1
fi

echo ""
echo "ПОНИЖАЕМ приоритет Ethernet..."
WIRED_CONN=$(nmcli -t -f NAME,TYPE connection show | grep ethernet | head -1 | cut -d: -f1)
if [ -n "$WIRED_CONN" ]; then
    nmcli connection modify "$WIRED_CONN" connection.autoconnect-priority -10
    nmcli connection modify "$WIRED_CONN" ipv4.route-metric 1000
    echo "Ethernet '$WIRED_CONN': приоритет=-10, метрика=1000"
else
    echo "Ethernet подключение не найдено"
fi

echo ""
echo "Перезапуск соединений..."
if [ -n "$WIRED_CONN" ]; then
    nmcli connection down "$WIRED_CONN" 2>/dev/null || true
    nmcli connection up "$WIRED_CONN" 2>/dev/null || true
fi
systemctl restart NetworkManager
sleep 5

echo ""
echo "Проверка подключения..."
if nmcli device status | grep -q "wlan0.*connected"; then
    echo "WiFi ПОДКЛЮЧЕН!"
    echo ""
    echo "Активное WiFi:"
    nmcli connection show --active | grep wifi
    echo ""
    echo "IP адрес WiFi:"
    ip addr show wlan0 | grep "inet " | awk '{print $2}'
    echo ""
    echo "Маршруты:"
    ip route | grep default
else
    echo "WiFi не подключен"
    echo "Доступные сети:"
    nmcli device wifi list
fi

echo ""
echo "========================================="
echo "  ГОТОВО!"
echo "========================================="
echo ""
echo "Приоритеты:"
echo "  WiFi: autoconnect-priority=100, route-metric=100"
echo "  Ethernet: autoconnect-priority=-10, route-metric=1000"
echo ""
echo "WiFi теперь ВСЕГДА в приоритете!"
echo ""
echo "Проверьте: ping google.com"
EOF
```

### Шаг 3: Сделайте исполняемым

```bash
chmod +x wifi-priority-setup.sh
```

### Шаг 4: Запустите

```bash
sudo ./wifi-priority-setup.sh
```

---

## 🧪 ПРОВЕРКА РАБОТЫ

### Посмотреть приоритеты

```bash
nmcli -f NAME,TYPE,AUTOCONNECT-PRIORITY connection show
```

### Посмотреть маршруты (ГЛАВНОЕ!)

```bash
ip route show
```

**Правильный результат:**
```
default via X.X.X.X dev wlan0 ... metric 100    ← WiFi ПЕРВЫМ!
default via X.X.X.X dev eth0 ... metric 1000    ← Ethernet вторым
```

### Проверить интернет

```bash
ping -c 4 google.com
```

---

## 💡 ПОЛЕЗНЫЕ КОМАНДЫ

### Посмотреть все настройки подключения

```bash
nmcli connection show "WiFi-1"
```

### Переключить WiFi сети вручную

```bash
sudo nmcli connection up WiFi-1
# или
sudo nmcli connection up WiFi-2
```

### Посмотреть доступные WiFi

```bash
nmcli device wifi list
```

### Изменить пароль WiFi

```bash
sudo nmcli connection modify WiFi-1 wifi-sec.psk "новый_пароль"
sudo systemctl restart NetworkManager
```

---

## 🔧 РЕШЕНИЕ ПРОБЛЕМ

### Проблема: Интернет всё равно идет через eth0

**Решение - установите метрики вручную:**

```bash
sudo nmcli connection modify "Wired connection 1" ipv4.route-metric 1000
sudo nmcli connection modify WiFi-1 ipv4.route-metric 100
sudo nmcli connection modify WiFi-2 ipv4.route-metric 100
sudo nmcli connection down "Wired connection 1"
sudo nmcli connection up "Wired connection 1"
sudo nmcli connection down WiFi-1
sudo nmcli connection up WiFi-1
ip route show
```

### Проблема: WiFi не подключается

```bash
sudo nmcli radio wifi on
nmcli device wifi list
sudo systemctl restart NetworkManager
```

### Проблема: Нет интернета

```bash
# Проверить DNS
nmcli device show wlan0 | grep DNS

# Если DNS нет, добавить Google DNS
sudo nmcli connection modify WiFi-1 ipv4.dns "8.8.8.8 8.8.4.4"
sudo nmcli connection down WiFi-1
sudo nmcli connection up WiFi-1
```

---

## 📊 ПОНИМАНИЕ ПРИОРИТЕТОВ

### autoconnect-priority
Определяет **какое подключение включается** при загрузке:
- WiFi: 100 (высокий) - включается первым
- Ethernet: -10 (низкий) - включается только если WiFi недоступен

### route-metric (ipv4.route-metric)
Определяет **через какой интерфейс идет трафик**:
- WiFi: 100 (низкая метрика) - используется для интернета
- Ethernet: 1000 (высокая метрика) - резервный канал

**ЧЕМ НИЖЕ МЕТРИКА - ТЕМ ВЫШЕ ПРИОРИТЕТ!**

---

## 📋 СХЕМА ПРИОРИТЕТОВ

```
┌──────────────────────────────────────────────┐
│  WiFi-1: priority=100, metric=100            │ ⬅️ Используется
├──────────────────────────────────────────────┤
│  WiFi-2: priority=90, metric=100             │ ⬅️ Запасная
├──────────────────────────────────────────────┤
│  Ethernet: priority=-10, metric=1000         │ ⬅️ Только если нет WiFi
└──────────────────────────────────────────────┘
```

---

## 🎯 СБРОС НАСТРОЕК

Если нужно удалить всё и настроить заново:

```bash
sudo nmcli connection delete WiFi-1
sudo nmcli connection delete WiFi-2
sudo nmcli connection modify "Wired connection 1" connection.autoconnect-priority 0
sudo nmcli connection modify "Wired connection 1" ipv4.route-metric 0
sudo systemctl restart NetworkManager
```

Затем запустите скрипт снова:

```bash
sudo ./wifi-priority-setup.sh
```

---

## ✅ ФИНАЛЬНАЯ ПРОВЕРКА

После настройки выполните:

```bash
echo "=== Приоритеты ==="
nmcli -f NAME,AUTOCONNECT-PRIORITY connection show
echo ""
echo "=== Маршруты ==="
ip route show
echo ""
echo "=== Активные подключения ==="
nmcli connection show --active
echo ""
echo "=== Тест интернета ==="
ping -c 4 8.8.8.8
```

**Правильный результат:**
- В маршрутах `wlan0` с метрикой 100 идет ПЕРВЫМ
- `eth0` с метрикой 1000 идет вторым
- Ping работает

---

## 🔑 КЛЮЧЕВЫЕ КОМАНДЫ ДЛЯ ЗАПОМИНАНИЯ

Изменить приоритет автоподключения:
```bash
sudo nmcli connection modify "ИМЯ" connection.autoconnect-priority ЧИСЛО
```

Изменить метрику маршрута:
```bash
sudo nmcli connection modify "ИМЯ" ipv4.route-metric ЧИСЛО
```

Перезапустить подключение:
```bash
sudo nmcli connection down "ИМЯ"
sudo nmcli connection up "ИМЯ"
```

---

**© 2025 Orange Pi WiFi Priority Setup**  
**Проверено с учетом route-metric!** ✅