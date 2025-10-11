#!/bin/bash

# Скрипт для исправления приоритета сети на Orange Pi
# WiFi должен иметь приоритет над Ethernet

echo "=== Текущие маршруты ==="
ip route show
echo ""

echo "=== Текущие соединения ==="
nmcli connection show
echo ""

# Получаем имена соединений
ETHERNET_CONN=$(nmcli -t -f NAME,TYPE connection show | grep ethernet | cut -d: -f1 | head -n1)
WIFI_CONN=$(nmcli -t -f NAME,TYPE connection show | grep wifi | cut -d: -f1 | head -n1)

echo "Найдено Ethernet соединение: $ETHERNET_CONN"
echo "Найдено WiFi соединение: $WIFI_CONN"
echo ""

# Настройка WiFi с высоким приоритетом
echo "=== Настройка WiFi (высокий приоритет) ==="
sudo nmcli connection modify "$WIFI_CONN" \
  connection.autoconnect-priority 100 \
  ipv4.route-metric 100 \
  ipv6.route-metric 100

# Настройка Ethernet с низким приоритетом
echo "=== Настройка Ethernet (низкий приоритет) ==="
sudo nmcli connection modify "$ETHERNET_CONN" \
  connection.autoconnect-priority -10 \
  ipv4.route-metric 1000 \
  ipv6.route-metric 1000

echo ""
echo "=== Перезапуск соединений ==="
sudo nmcli connection down "$ETHERNET_CONN"
sudo nmcli connection down "$WIFI_CONN"
sleep 2
sudo nmcli connection up "$WIFI_CONN"
sudo nmcli connection up "$ETHERNET_CONN"

echo ""
echo "=== Новые маршруты (WiFi должен быть первым) ==="
sleep 3
ip route show

echo ""
echo "=== Проверка приоритетов ==="
nmcli -f NAME,AUTOCONNECT-PRIORITY connection show

echo ""
echo "✅ Готово! WiFi теперь имеет приоритет над Ethernet"
echo "Проверьте доступность: http://192.168.3.25:3000"
