# Ручная установка WiFi автоподключения на Orange Pi через SSH

## Алгоритм без использования GitHub

Эта инструкция описывает, как перенести скрипт WiFi автоподключения напрямую на Orange Pi через SSH.

---

## Вариант 1: Копирование через SCP (рекомендуется)

### Шаг 1: Узнайте IP адрес Orange Pi

На Orange Pi выполните:
```bash
ip addr show wlan0 | grep "inet "
# или
hostname -I
```

Запомните IP адрес (например: `192.168.1.100`)

### Шаг 2: С вашего компьютера скопируйте скрипт на Orange Pi

Откройте командную строку на Windows (в папке проекта):

```bash
# Перейдите в папку с проектом
cd C:\Users\timur\Desktop\portfolio

# Скопируйте скрипт на Orange Pi
scp deploy\setup-dual-wifi.sh root@192.168.1.100:/root/

# Если нужно указать пароль - введите его
```

**Замените:**
- `192.168.1.100` - на реальный IP вашего Orange Pi
- `root` - на вашего пользователя (обычно `root` или `orangepi`)

### Шаг 3: Подключитесь к Orange Pi через SSH

```bash
ssh root@192.168.1.100
```

### Шаг 4: На Orange Pi выполните установку

```bash
# Сделайте скрипт исполняемым
chmod +x /root/setup-dual-wifi.sh

# Запустите скрипт
./setup-dual-wifi.sh
```

Следуйте инструкциям на экране - введите SSID и пароли обеих WiFi сетей.

---

## Вариант 2: Создание скрипта прямо на Orange Pi

Если SCP не работает, можно создать файл напрямую на Orange Pi.

### Шаг 1: Подключитесь к Orange Pi через SSH

```bash
ssh root@192.168.1.100
```

### Шаг 2: Создайте файл скрипта

```bash
nano /root/setup-dual-wifi.sh
```

### Шаг 3: Скопируйте содержимое скрипта

Откройте файл `C:\Users\timur\Desktop\portfolio\deploy\setup-dual-wifi.sh` на вашем компьютере, скопируйте весь текст и вставьте в nano:

**Нажмите:**
- `Ctrl+Shift+V` для вставки
- `Ctrl+X` для выхода
- `Y` для сохранения
- `Enter` для подтверждения

### Шаг 4: Сделайте скрипт исполняемым и запустите

```bash
chmod +x /root/setup-dual-wifi.sh
./setup-dual-wifi.sh
```

---

## Вариант 3: Через USB или карту памяти

Если нет сетевого подключения вообще:

### Шаг 1: Скопируйте файл на USB флешку

На вашем компьютере:
1. Скопируйте файл `C:\Users\timur\Desktop\portfolio\deploy\setup-dual-wifi.sh` на USB флешку
2. Безопасно извлеките USB

### Шаг 2: Подключите USB к Orange Pi

1. Вставьте USB флешку в Orange Pi
2. Подключитесь к Orange Pi через монитор и клавиатуру (или SSH если есть Ethernet)

### Шаг 3: Примонтируйте USB

```bash
# Найдите устройство USB
lsblk

# Обычно это /dev/sda1 или /dev/sdb1
# Создайте точку монтирования
mkdir -p /mnt/usb

# Примонтируйте USB
mount /dev/sda1 /mnt/usb

# Проверьте содержимое
ls -la /mnt/usb
```

### Шаг 4: Скопируйте скрипт и запустите

```bash
# Скопируйте скрипт
cp /mnt/usb/setup-dual-wifi.sh /root/

# Размонтируйте USB
umount /mnt/usb

# Сделайте скрипт исполняемым
chmod +x /root/setup-dual-wifi.sh

# Запустите
./setup-dual-wifi.sh
```

---

## Вариант 4: Через временное Ethernet подключение

Если на Orange Pi есть Ethernet порт:

### Шаг 1: Подключите Orange Pi к роутеру через кабель

Просто подключите Ethernet кабель от роутера к Orange Pi.

### Шаг 2: Узнайте IP адрес

На роутере посмотрите список подключенных устройств или на Orange Pi:
```bash
ip addr show eth0 | grep "inet "
```

### Шаг 3: Используйте Вариант 1 (SCP)

Теперь можно использовать SCP для копирования файла (см. Вариант 1 выше).

---

## Полный алгоритм действий

### Что нужно сделать на вашем компьютере (Windows):

1. **Откройте командную строку** (Win+R → `cmd`)

2. **Перейдите в папку проекта:**
   ```cmd
   cd C:\Users\timur\Desktop\portfolio
   ```

3. **Узнайте IP Orange Pi** (если не знаете):
   - Посмотрите в настройках роутера список подключенных устройств
   - Или подключитесь к Orange Pi через монитор и выполните: `hostname -I`

4. **Скопируйте скрипт на Orange Pi:**
   ```cmd
   scp deploy\setup-dual-wifi.sh root@192.168.1.100:/root/
   ```

   Замените `192.168.1.100` на реальный IP вашего Orange Pi.

5. **Подключитесь к Orange Pi:**
   ```cmd
   ssh root@192.168.1.100
   ```

### Что нужно сделать на Orange Pi (через SSH):

1. **Сделайте скрипт исполняемым:**
   ```bash
   chmod +x /root/setup-dual-wifi.sh
   ```

2. **Запустите скрипт:**
   ```bash
   sudo ./setup-dual-wifi.sh
   ```

3. **Введите данные WiFi сетей:**
   - SSID первой сети (работа)
   - Пароль первой сети
   - SSID второй сети (дом)
   - Пароль второй сети
   - Выберите приоритет (какая сеть важнее)

4. **Проверьте подключение:**
   ```bash
   nmcli device status
   nmcli connection show
   ```

5. **Перезагрузите для проверки автоподключения:**
   ```bash
   sudo reboot
   ```

После перезагрузки Orange Pi должен автоматически подключиться к доступной WiFi сети!

---

## Проверка после установки

### После выполнения скрипта:

```bash
# Проверить настроенные подключения
nmcli connection show

# Должны появиться:
# WiFi-Location1
# WiFi-Location2

# Проверить статус WiFi
nmcli device status

# Должно показать:
# wlan0   wifi   connected   WiFi-Location1

# Проверить IP адрес
ip addr show wlan0 | grep "inet "
```

### Тестирование переключения:

```bash
# Попробуйте переключиться между сетями
nmcli connection down WiFi-Location1
nmcli connection up WiFi-Location2

# Проверьте статус
nmcli device status
```

---

## Что делать если SCP не работает

### Ошибка: "ssh: connect to host 192.168.1.100 port 22: Connection refused"

**Решение 1:** Проверьте, установлен ли SSH сервер на Orange Pi
```bash
# На Orange Pi
sudo apt-get update
sudo apt-get install -y openssh-server
sudo systemctl enable ssh
sudo systemctl start ssh
```

**Решение 2:** Проверьте, правильный ли IP адрес
```bash
# На Orange Pi
hostname -I
ip addr show
```

**Решение 3:** Проверьте firewall
```bash
# На Orange Pi
sudo ufw status
# Если активен:
sudo ufw allow 22/tcp
```

### Ошибка: "Permission denied"

**Решение:** Используйте правильное имя пользователя
```bash
# Попробуйте разные варианты:
scp deploy\setup-dual-wifi.sh orangepi@192.168.1.100:/home/orangepi/
scp deploy\setup-dual-wifi.sh root@192.168.1.100:/root/
```

---

## Упрощенная команда (все в одном)

После того как скопировали файл на Orange Pi, выполните одной командой:

```bash
ssh root@192.168.1.100 "chmod +x /root/setup-dual-wifi.sh && /root/setup-dual-wifi.sh"
```

Это:
1. Подключится к Orange Pi
2. Сделает скрипт исполняемым
3. Запустит его

---

## Полезные команды для Windows

### Проверка доступности Orange Pi:

```cmd
ping 192.168.1.100
```

### Проверка SSH соединения:

```cmd
ssh -v root@192.168.1.100
```

### Копирование с указанием порта (если SSH на нестандартном порту):

```cmd
scp -P 2222 deploy\setup-dual-wifi.sh root@192.168.1.100:/root/
```

---

## Альтернатива: WinSCP (графический интерфейс)

Если не хотите использовать командную строку:

1. **Скачайте WinSCP:** https://winscp.net/
2. **Установите и запустите**
3. **Подключитесь к Orange Pi:**
   - Protocol: SCP
   - Host: 192.168.1.100
   - User: root
   - Password: (ваш пароль)
4. **Перетащите файл** `setup-dual-wifi.sh` в папку `/root/`
5. **Подключитесь через SSH** и выполните:
   ```bash
   chmod +x /root/setup-dual-wifi.sh
   ./setup-dual-wifi.sh
   ```

---

## Резюме

**Самый простой способ:**
1. Узнайте IP Orange Pi
2. Выполните на Windows: `scp deploy\setup-dual-wifi.sh root@IP:/root/`
3. Подключитесь через SSH: `ssh root@IP`
4. Запустите: `chmod +x /root/setup-dual-wifi.sh && sudo ./setup-dual-wifi.sh`
5. Следуйте инструкциям скрипта

**После настройки:**
- Orange Pi будет автоматически подключаться к WiFi при загрузке
- Можно перевозить между локациями - подключится к доступной сети
- Настройки сохраняются после перезагрузки

---

## Контактная информация файлов

На вашем компьютере файлы находятся:
- `C:\Users\timur\Desktop\portfolio\deploy\setup-dual-wifi.sh` - основной скрипт
- `C:\Users\timur\Desktop\portfolio\deploy\WIFI-AUTO-CONNECT.md` - документация Markdown
- `C:\Users\timur\Desktop\portfolio\deploy\WIFI-AUTO-CONNECT.html` - документация HTML

На Orange Pi после копирования:
- `/root/setup-dual-wifi.sh` - скрипт установки
- `/etc/NetworkManager/system-connections/WiFi-Location1.nmconnection` - конфигурация первой сети
- `/etc/NetworkManager/system-connections/WiFi-Location2.nmconnection` - конфигурация второй сети
