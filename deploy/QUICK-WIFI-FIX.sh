#!/bin/bash
# Быстрое исправление нестабильного WiFi на Orange Pi
# Запустите: sudo ./QUICK-WIFI-FIX.sh

echo "🔧 Быстрое исправление WiFi на Orange Pi"
echo ""

if [ "$EUID" -ne 0 ]; then
    echo "❌ Ошибка: Запустите с sudo"
    exit 1
fi

# 1. Отключить Power Management
echo "[1/3] Отключение Power Management..."
iwconfig wlan0 power off 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✅ Power Management отключен"
else
    echo "⚠️  iwconfig недоступен, пропускаем"
fi

# 2. Создать systemd service
echo "[2/3] Создание systemd service..."
cat > /etc/systemd/system/wifi-power-off.service << 'EOF'
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

systemctl daemon-reload
systemctl enable wifi-power-off.service
systemctl start wifi-power-off.service
echo "✅ Systemd service создан"

# 3. Настроить NetworkManager
echo "[3/3] Настройка NetworkManager..."
mkdir -p /etc/NetworkManager/conf.d

cat > /etc/NetworkManager/conf.d/wifi-stability.conf << 'EOF'
[connection]
wifi.powersave = 2

[device]
wifi.scan-rand-mac-address = no
EOF

# Применить настройки к существующим WiFi подключениям
WIFI_CONN=$(nmcli -t -f NAME,TYPE connection show | grep wifi | head -1 | cut -d: -f1)
if [ -n "$WIFI_CONN" ]; then
    nmcli connection modify "$WIFI_CONN" wifi.powersave 2 2>/dev/null || true
    nmcli connection modify "$WIFI_CONN" connection.auth-retries 0 2>/dev/null || true
    echo "✅ Настройки применены к '$WIFI_CONN'"
fi

# Перезапустить NetworkManager
systemctl restart NetworkManager
sleep 3

echo ""
echo "======================================"
echo "✅ ГОТОВО!"
echo "======================================"
echo ""
echo "Проверка:"
iwconfig wlan0 2>/dev/null | grep "Power Management" || echo "iwconfig недоступен"
echo ""
echo "Сервис:"
systemctl is-active wifi-power-off.service
echo ""
echo "Перезагрузите Orange Pi для полного применения настроек:"
echo "  sudo reboot"
echo ""
