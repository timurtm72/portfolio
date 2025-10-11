#!/bin/bash

# Скрипт для постоянного исправления приоритета маршрутов
# WiFi должен иметь приоритет над Ethernet

echo "=== Исправление маршрутов WiFi/Ethernet ==="

# Удаляем старый маршрут Ethernet без метрики
sudo ip route del default via 192.168.1.1 dev eth0 2>/dev/null

# Добавляем маршрут Ethernet с низким приоритетом
sudo ip route add default via 192.168.1.1 dev eth0 metric 1000 2>/dev/null

echo "Готово!"
ip route show | grep default
