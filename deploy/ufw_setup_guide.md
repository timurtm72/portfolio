# Настройка UFW на Debian 11 (Orange Pi)

В этой сессии была проведена настройка брандмауэра UFW на Orange Pi с Debian 11. Ниже подробно описаны все шаги и решения, которые были выполнены.

---

## 1. Установка UFW

```bash
sudo apt update
sudo apt install ufw -y
```

Проблема: репозиторий `bullseye-backports` не доступен, но установка пакета `ufw` прошла успешно.

---

## 2. Первоначальные попытки включения UFW

```bash
sudo ufw default allow outgoing
sudo ufw allow 22/tcp comment 'SSH'
sudo ufw allow 80/tcp comment 'HTTP'
sudo ufw allow 443/tcp comment 'HTTPS'
sudo ufw --force enable
```

Ошибка: 
- `iptables-restore unknown option "--log-prefix"`
- `ip6tables-restore couldn't load match rt`

Причина: использование устаревших или несовместимых правил логирования и IPv6.

---

## 3. Проверка конфигурационных файлов

### Файлы, которые были проверены и исправлены:

- `/etc/ufw/user.rules` (IPv4)
- `/etc/ufw/user6.rules` (IPv6)
- `/etc/ufw/ufw.conf`

Были удалены или закомментированы проблемные правила логирования `--log-prefix` и правила IPv6 с `--rt-type`.

---

## 4. Финальная конфигурация файлов

### `/etc/ufw/ufw.conf`
```
ENABLED=yes
LOGLEVEL=low
```

### `/etc/ufw/user.rules` (IPv4)
```
*filter
:ufw-user-input - [0:0]
:ufw-user-output - [0:0]
:ufw-user-forward - [0:0]

-A ufw-user-input -p tcp --dport 22 -j ACCEPT   # SSH
-A ufw-user-input -p tcp --dport 80 -j ACCEPT   # HTTP
-A ufw-user-input -p tcp --dport 443 -j ACCEPT  # HTTPS

COMMIT
```

### `/etc/ufw/user6.rules` (IPv6)
```
*filter
:ufw6-user-input - [0:0]
:ufw6-user-output - [0:0]
:ufw6-user-forward - [0:0]

-A ufw6-user-input -p tcp --dport 22 -j ACCEPT   # SSH
-A ufw6-user-input -p tcp --dport 80 -j ACCEPT   # HTTP
-A ufw6-user-input -p tcp --dport 443 -j ACCEPT  # HTTPS

COMMIT
```

---

## 5. Включение и проверка UFW

```bash
sudo ufw disable
sudo ufw --force enable
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw status verbose
```

Пример вывода:
```
Status: active
Logging: low
Default: deny (incoming), allow (outgoing), deny (routed)

To                         Action      From
22/tcp                     ALLOW IN    Anywhere
80/tcp                     ALLOW IN    Anywhere
443/tcp                    ALLOW IN    Anywhere
22/tcp (v6)                ALLOW IN    Anywhere (v6)
80/tcp (v6)                ALLOW IN    Anywhere (v6)
443/tcp (v6)               ALLOW IN    Anywhere (v6)
```

---

## 6. Тонкости и решения

1. Проблемы с `--log-prefix` были решены удалением/заменой правил логирования.
2. Проблемы с `--rt-type` для IPv6 были решены удалением соответствующих правил.
3. Логи отключены (`LOGLEVEL=low`) для совместимости с Debian 11 и Orange Pi.
4. Убедились, что UFW включен при старте системы (`ENABLED=yes`).
5. Проверено, что правила применяются корректно и SSH, HTTP, HTTPS работают по IPv4 и IPv6.

---

Файл содержит все шаги, которые были выполнены в этой сессии для настройки UFW с учетом всех тонкостей Debian 11 и проблем с IPv6 и логированием.