# Автоматическое подключение WiFi на Orange Pi для двух локаций

## Описание проблемы

При включении Orange Pi Zero 3 WiFi не подключается автоматически. Требуется настроить автоматическое подключение к двум разным WiFi сетям (например, работа и дом).

## Решение

Используем NetworkManager для автоматического подключения с приоритетами:
- При загрузке Orange Pi автоматически подключится к доступной сети
- Если обе сети доступны - подключится к сети с более высоким приоритетом
- Настройки сохраняются после перезагрузки

## Быстрый старт

### 1. Скопируйте скрипт на Orange Pi

```bash
# Вариант 1: Если Orange Pi уже подключен к WiFi вручную
cd /opt/portfolio
git pull

# Вариант 2: Если нет подключения - скопируйте файл вручную
# Например, через USB или временное Ethernet подключение
```

### 2. Запустите скрипт настройки

```bash
cd /opt/portfolio/deploy
chmod +x setup-dual-wifi.sh
sudo ./setup-dual-wifi.sh
```

### 3. Следуйте инструкциям на экране

Скрипт попросит ввести:
1. **SSID первой WiFi сети** (например, название роутера на работе)
2. **Пароль первой сети**
3. **SSID второй WiFi сети** (например, название домашнего роутера)
4. **Пароль второй сети**
5. **Приоритет** (какая сеть важнее, если обе доступны)

### 4. Готово!

После настройки Orange Pi будет автоматически подключаться к доступной WiFi при каждой загрузке.

## Что делает скрипт?

1. **Устанавливает NetworkManager** (если не установлен)
2. **Создает два WiFi профиля** с настройками:
   - `connection.autoconnect = yes` - автоподключение при загрузке
   - `connection.autoconnect-priority` - приоритет (100 или 50)
3. **Сохраняет настройки** в `/etc/NetworkManager/system-connections/`
4. **Перезапускает NetworkManager** для применения изменений
5. **Проверяет подключение**

## Проверка работы

### Просмотр настроенных сетей

```bash
nmcli connection show
```

Вы увидите:
```
NAME              UUID                                  TYPE  DEVICE
WiFi-Location1    xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx  wifi  wlan0
WiFi-Location2    yyyyyyyy-yyyy-yyyy-yyyy-yyyyyyyyyyyy  wifi  --
```

### Статус подключения

```bash
nmcli device status
```

Должно показать:
```
DEVICE  TYPE      STATE        CONNECTION
wlan0   wifi      connected    WiFi-Location1
```

### Информация об активном подключении

```bash
nmcli connection show --active | grep wifi
ip addr show wlan0
```

## Полезные команды

### Управление подключениями

```bash
# Показать все доступные WiFi сети
nmcli device wifi list

# Принудительно подключиться к первой локации
nmcli connection up WiFi-Location1

# Принудительно подключиться ко второй локации
nmcli connection up WiFi-Location2

# Отключиться от WiFi
nmcli connection down WiFi-Location1

# Переподключиться
nmcli connection down WiFi-Location1 && nmcli connection up WiFi-Location1
```

### Редактирование существующих подключений

```bash
# Изменить пароль для первой локации
nmcli connection modify WiFi-Location1 wifi-sec.psk "новый_пароль"

# Изменить приоритет
nmcli connection modify WiFi-Location1 connection.autoconnect-priority 100

# Применить изменения
sudo systemctl restart NetworkManager
```

### Удаление подключений

```bash
# Удалить подключение
nmcli connection delete WiFi-Location1

# Запустить скрипт заново для настройки
sudo ./setup-dual-wifi.sh
```

## Логи и отладка

### Просмотр логов NetworkManager

```bash
# Логи в реальном времени
journalctl -u NetworkManager -f

# Последние 50 строк логов
journalctl -u NetworkManager -n 50

# Логи за последний час
journalctl -u NetworkManager --since "1 hour ago"
```

### Перезапуск NetworkManager

```bash
sudo systemctl restart NetworkManager

# Проверка статуса
sudo systemctl status NetworkManager
```

## Устранение проблем

### WiFi не подключается после настройки

1. **Проверьте правильность SSID и пароля**
   ```bash
   nmcli connection show WiFi-Location1 | grep -E "ssid|psk"
   ```

2. **Проверьте, видит ли Orange Pi сети**
   ```bash
   nmcli device wifi list
   ```

3. **Проверьте статус WiFi адаптера**
   ```bash
   nmcli radio wifi
   # Должно быть: enabled

   # Если disabled, включите:
   nmcli radio wifi on
   ```

4. **Перезапустите NetworkManager**
   ```bash
   sudo systemctl restart NetworkManager
   sleep 5
   nmcli device status
   ```

### WiFi отключается через некоторое время

Отключите энергосбережение WiFi:

```bash
# Создайте файл конфигурации
sudo nano /etc/NetworkManager/conf.d/wifi-powersave.conf
```

Добавьте:
```ini
[connection]
wifi.powersave = 2
```

Перезапустите:
```bash
sudo systemctl restart NetworkManager
```

### Нужно добавить третью WiFi сеть

```bash
# Добавьте вручную
sudo nmcli connection add \
    type wifi \
    con-name "WiFi-Location3" \
    ifname wlan0 \
    ssid "SSID_третьей_сети" \
    wifi-sec.key-mgmt wpa-psk \
    wifi-sec.psk "пароль_третьей_сети" \
    connection.autoconnect yes \
    connection.autoconnect-priority 30

# Перезапустите NetworkManager
sudo systemctl restart NetworkManager
```

## Автоматическое переключение между сетями

NetworkManager автоматически:
- **При загрузке**: подключается к доступной сети с наивысшим приоритетом
- **При потере сигнала**: переключается на другую доступную сеть
- **При перемещении**: автоматически подключается к ближайшей сети из списка

Например:
1. Утром включили Orange Pi на работе → подключился к WiFi-Location1
2. Вечером принесли домой, перезагрузили → подключился к WiFi-Location2
3. На следующий день принесли обратно → подключился к WiFi-Location1

## Файлы конфигурации

Настройки хранятся в:
```
/etc/NetworkManager/system-connections/WiFi-Location1.nmconnection
/etc/NetworkManager/system-connections/WiFi-Location2.nmconnection
```

**Внимание:** Файлы содержат пароли в зашифрованном виде. Не делитесь ими!

## Безопасность

Права доступа к файлам настроек автоматически устанавливаются в `600` (только root может читать). Это защищает ваши пароли WiFi.

```bash
# Проверка прав
ls -la /etc/NetworkManager/system-connections/

# Должно быть: -rw------- root root
```

## Интеграция с Docker

После настройки WiFi ваш Docker контейнер с портфолио будет доступен в обеих локациях:

```bash
# Узнать IP адрес Orange Pi
ip addr show wlan0 | grep "inet "

# Порты (из docker-compose.yml):
# - Frontend: http://<IP>:3000
# - Backend: http://<IP>:8080/api
# - PostgreSQL: <IP>:5432 (только для локальных подключений)
```

## Проброс портов на роутере

Для доступа извне настройте проброс портов на **обоих** роутерах:

| Внешний порт | Внутренний порт | Протокол | Назначение |
|--------------|-----------------|----------|------------|
| 80           | 3000            | TCP      | Frontend   |
| 8080         | 8080            | TCP      | Backend    |
| 22           | 22              | TCP      | SSH        |

**Важно:** IP адрес Orange Pi может меняться в разных локациях!

### Решение 1: Статический IP в каждой локации

На каждом роутере настройте резервирование DHCP по MAC адресу:
- Локация 1: 192.168.1.100
- Локация 2: 192.168.0.100

### Решение 2: DynamicDNS

Используйте сервис DynamicDNS (например, No-IP, DuckDNS) для постоянного доменного имени.

## Мониторинг подключения

Создайте простой скрипт мониторинга:

```bash
#!/bin/bash
# ~/check-wifi.sh

while true; do
    if ! nmcli device status | grep -q "connected"; then
        echo "$(date): WiFi отключен, переподключаюсь..."
        nmcli connection up WiFi-Location1 || nmcli connection up WiFi-Location2
    fi
    sleep 60
done
```

Запуск в фоне:
```bash
chmod +x ~/check-wifi.sh
nohup ~/check-wifi.sh > ~/wifi-monitor.log 2>&1 &
```

## Дополнительная информация

- [NetworkManager Documentation](https://networkmanager.dev/)
- [nmcli Examples](https://developer.gnome.org/NetworkManager/stable/nmcli-examples.html)
- [Orange Pi Zero 3 Wiki](http://www.orangepi.org/html/hardWare/computerAndMicrocontrollers/details/Orange-Pi-Zero-3.html)

## Поддержка

Если возникли проблемы:
1. Проверьте логи: `journalctl -u NetworkManager -n 100`
2. Убедитесь, что WiFi адаптер включен: `nmcli radio wifi`
3. Проверьте видимость сетей: `nmcli device wifi list`
4. Попробуйте подключиться вручную: `nmcli connection up WiFi-Location1`

Для сброса всех настроек и настройки заново:
```bash
nmcli connection delete WiFi-Location1
nmcli connection delete WiFi-Location2
sudo ./setup-dual-wifi.sh
```
