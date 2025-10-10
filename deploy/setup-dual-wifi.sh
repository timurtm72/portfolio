#!/bin/bash

# Скрипт автоматической настройки WiFi для двух локаций на Orange Pi Zero 3
# Использует NetworkManager для автоматического подключения к доступной сети

set -e

echo "=== Настройка автоматического WiFi подключения для двух локаций ==="
echo ""

# Проверка прав root
if [ "$EUID" -ne 0 ]; then
    echo "Ошибка: Запустите скрипт с правами root (sudo)"
    exit 1
fi

# Функция для добавления WiFi сети
add_wifi_network() {
    local SSID=$1
    local PASSWORD=$2
    local PRIORITY=$3
    local CONNECTION_NAME=$4

    echo "Добавление сети: $CONNECTION_NAME (SSID: $SSID, приоритет: $PRIORITY)"

    # Удаляем существующее подключение если есть
    nmcli connection delete "$CONNECTION_NAME" 2>/dev/null || true

    # Создаем новое подключение
    nmcli connection add \
        type wifi \
        con-name "$CONNECTION_NAME" \
        ifname wlan0 \
        ssid "$SSID" \
        wifi-sec.key-mgmt wpa-psk \
        wifi-sec.psk "$PASSWORD" \
        connection.autoconnect yes \
        connection.autoconnect-priority $PRIORITY

    echo "✓ Сеть $CONNECTION_NAME добавлена"
    echo ""
}

# Запрос данных для первой локации (работа)
echo "=== ЛОКАЦИЯ 1 (например, работа) ==="
read -p "Введите SSID первой WiFi сети: " WIFI1_SSID
read -sp "Введите пароль первой WiFi сети: " WIFI1_PASSWORD
echo ""
echo ""

# Запрос данных для второй локации (дом)
echo "=== ЛОКАЦИЯ 2 (например, дом) ==="
read -p "Введите SSID второй WiFi сети: " WIFI2_SSID
read -sp "Введите пароль второй WiFi сети: " WIFI2_PASSWORD
echo ""
echo ""

# Запрос приоритета (какая сеть важнее, если обе доступны)
echo "=== ПРИОРИТЕТЫ ==="
echo "Если обе сети будут доступны одновременно, к какой подключаться?"
echo "1) К первой сети ($WIFI1_SSID)"
echo "2) Ко второй сети ($WIFI2_SSID)"
read -p "Выберите (1 или 2): " PRIORITY_CHOICE
echo ""

if [ "$PRIORITY_CHOICE" == "1" ]; then
    WIFI1_PRIORITY=100
    WIFI2_PRIORITY=50
    echo "Приоритет отдан первой сети"
else
    WIFI1_PRIORITY=50
    WIFI2_PRIORITY=100
    echo "Приоритет отдан второй сети"
fi
echo ""

# Установка NetworkManager если не установлен
if ! command -v nmcli &> /dev/null; then
    echo "Установка NetworkManager..."
    apt-get update
    apt-get install -y network-manager
    systemctl enable NetworkManager
    systemctl start NetworkManager
    echo "✓ NetworkManager установлен"
    echo ""
fi

# Добавление обеих сетей
add_wifi_network "$WIFI1_SSID" "$WIFI1_PASSWORD" $WIFI1_PRIORITY "WiFi-Location1"
add_wifi_network "$WIFI2_SSID" "$WIFI2_PASSWORD" $WIFI2_PRIORITY "WiFi-Location2"

# Перезапуск NetworkManager для применения настроек
echo "Перезапуск NetworkManager..."
systemctl restart NetworkManager
sleep 3
echo "✓ NetworkManager перезапущен"
echo ""

# Попытка подключения
echo "Попытка подключения к доступной сети..."
sleep 5

# Проверка подключения
if nmcli device status | grep -q "connected"; then
    echo "✓ Успешно подключено к WiFi!"
    echo ""
    echo "Информация о подключении:"
    nmcli connection show --active | grep wifi
    echo ""
    ip addr show wlan0 | grep "inet "
else
    echo "⚠ Подключение не установлено. Проверьте:"
    echo "  1. Правильность введенных SSID и паролей"
    echo "  2. Находитесь ли вы в зоне действия хотя бы одной из сетей"
    echo ""
    echo "Текущий статус:"
    nmcli device status
fi

echo ""
echo "=== НАСТРОЙКА ЗАВЕРШЕНА ==="
echo ""
echo "Теперь Orange Pi будет автоматически подключаться к доступной WiFi сети при загрузке."
echo ""
echo "Полезные команды для управления:"
echo "  nmcli connection show              - показать все подключения"
echo "  nmcli device wifi list             - показать доступные WiFi сети"
echo "  nmcli device status                - статус сетевых интерфейсов"
echo "  nmcli connection up WiFi-Location1 - принудительно подключиться к локации 1"
echo "  nmcli connection up WiFi-Location2 - принудительно подключиться к локации 2"
echo ""
