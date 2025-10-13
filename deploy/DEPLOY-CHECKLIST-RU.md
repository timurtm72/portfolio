# Памятка по развёртыванию и защите (Orange Pi, Debian 11, Docker + Caddy)

Эта памятка фиксирует рабочую конфигурацию, шаги настройки и диагностику, выполненные при запуске проекта. Сценарий: входной трафик идёт только через Caddy; `frontend`, `backend` и БД доступны только во внутренней сети Docker.

## Архитектура (итог)

```
Интернет / ЛВС
  ↓
Caddy :80 (reverse proxy + заголовки)
  ↓
Frontend :80 (внутри Docker)
  ↓
Backend :8080 (внутри Docker)
  ↓
PostgreSQL :5432 (внутри Docker)
```

- Снаружи открыты только порты: `22/tcp` (SSH, при необходимости) и `80/tcp` (HTTP через Caddy).
- `443` временно закрыт. Варианты включения HTTPS — см. ниже.

---

## Ключевые файлы и выдержки

### docker-compose.yml (публикует только 80 у Caddy)

```yaml
services:
  caddy:
    image: caddy:alpine
    container_name: portfolio-caddy
    restart: unless-stopped
    ports:
      - "80:80"
      # Для HTTPS позже можно вернуть:
      # - "443:443"
      # - "443:443/udp"  # HTTP/3
    volumes:
      - ./Caddyfile:/etc/caddy/Caddyfile:ro
      - caddy_data:/data
      - caddy_config:/config
    depends_on: [frontend, backend]
    networks: [portfolio_network]

  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: portfolio-frontend
    restart: unless-stopped
    expose: ["80", "3000"] # фронт реально слушает 80 (nginx)
    networks: [portfolio_network]

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: portfolio-backend
    restart: unless-stopped
    env_file:
      - ./backend/.env
    expose: ["8080"]
    depends_on: [db]
    networks: [portfolio_network]

  db:
    image: postgres:15-alpine
    container_name: portfolio-db
    restart: unless-stopped
    environment:
      POSTGRES_DB: portfolio
      POSTGRES_USER: portfolio_user
      POSTGRES_PASSWORD: changeme
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks: [portfolio_network]

networks:
  portfolio_network:
    driver: bridge

volumes:
  caddy_data:
  caddy_config:
  postgres_data:
```

Примечание: строка `version: "3.x"` не обязательна (в Docker Compose v2 можно её удалить).

### Caddyfile (HTTP‑only сейчас)

```caddy
:80 {
  header {
    X-Frame-Options "DENY"
    X-Content-Type-Options "nosniff"
    X-XSS-Protection "1; mode=block"
    Referrer-Policy "strict-origin-when-cross-origin"
    -Server
  }

  @api_all path /api*
  handle @api_all {
    reverse_proxy backend:8080
  }

  handle {
    reverse_proxy frontend:80
  }
}
```

---

## Что сделали (по шагам)

1) Переход в проект и проверка
- `cd ~/portfolio`
- `docker compose config` — проверка синтаксиса.

2) Настроили маршрутизацию и порты
- Снаружи публикует порты только `caddy`.
- `frontend`, `backend`, `db` — без `ports`, только `expose` и общая сеть `portfolio_network`.
- В `Caddyfile` убрали неподдерживаемые директивы (например, `rate_limit`) и настроили правила:
  - `/api*` → `backend:8080`
  - остальное → `frontend:80`

3) База данных
- Синхронизировали пароль БД (временно `changeme`, рекомендуем заменить).
- Создали БД `portfolio_db`:
  - `docker compose exec db psql -U "$POSTGRES_USER" -d postgres -c "CREATE DATABASE portfolio_db OWNER $POSTGRES_USER;"`

4) Запуск и валидация
- `docker compose up -d --build`
- Проверка Caddy: `docker compose exec caddy caddy validate --config /etc/caddy/Caddyfile` → `caddy reload`.
- Тесты:
  - `curl -I http://127.0.0.1/` → 200/304
  - `curl -I http://127.0.0.1/api/actuator/health` → 200 OK / `{ "status": "UP" }`

5) Брандмауэр (UFW)
- Разрешили: `22/tcp`, `80/tcp`; удалили `443/tcp`.
- Команды:
  - `sudo ufw allow 80/tcp && sudo ufw limit 22/tcp`
  - `sudo ufw status numbered`
  - Удаление правила: `sudo ufw delete <номер>`

---

## Включение HTTPS (варианты)

### Вариант A — публичный HTTPS с доменом (рекомендуется)
1) Роутер → проброс:
- 80/TCP → 192.168.1.100:80
- 443/TCP → 192.168.1.100:443
- (опц.) 443/UDP → 192.168.1.100:443 (HTTP/3)

2) UFW: `sudo ufw allow 443/tcp`

3) Docker Compose: вернуть публикацию 443 у `caddy` (`"443:443"`, опц. `"443:443/udp"`).

4) DNS: A‑запись домена → внешний IP.

5) Caddyfile под домен (Let’s Encrypt/ZeroSSL автоматически)

```caddy
{
  email you@example.com
}

:80 {
  redir https://{host}{uri} 308
}

example.com {
  encode zstd gzip
  header {
    Strict-Transport-Security "max-age=31536000; includeSubDomains; preload"
    X-Content-Type-Options "nosniff"
    X-Frame-Options "SAMEORIGIN"
    Referrer-Policy "no-referrer-when-downgrade"
    Permissions-Policy "geolocation=(), microphone=(), camera()"
    -Server
  }
  @api path /api*
  handle @api { reverse_proxy backend:8080 }
  handle { reverse_proxy frontend:80 }
  log {
    output file /var/log/caddy/portfolio-access.log
    format json
  }
}
```

- Применить: `caddy validate` → `caddy reload`.
- Проверить: `curl -I https://example.com/` (валидный сертификат и 200/304).

### Вариант B — временный self‑signed (без домена)

```caddy
:80 {
  redir https://{host}{uri} 308
}

:443 {
  tls internal
  encode zstd gzip
  header {
    Strict-Transport-Security "max-age=31536000"
    X-Content-Type-Options "nosniff"
    X-Frame-Options "SAMEORIGIN"
    Referrer-Policy "no-referrer-when-downgrade"
    Permissions-Policy "geolocation=(), microphone=(), camera()"
    -Server
  }
  @api path /api*
  handle @api { reverse_proxy backend:8080 }
  handle { reverse_proxy frontend:80 }
}
```

- Будет предупреждение в браузере (self‑signed). Устранить: установить корневой сертификат Caddy на ПК:
  - `docker compose exec caddy sh -lc 'cat /data/caddy/pki/authorities/local/root.crt' > root.crt`
  - Импортировать `root.crt` в доверенные корневые сертификаты ОС.
- Лучше обращаться по имени (например, `portfolio.local`) и прописать его в `hosts` клиентов → сертификат будет на имя.

---

## Диагностика и обслуживание

- Контейнеры/порты: `docker compose ps`
- Логи:
  - Caddy: `docker compose logs -f caddy`
  - Backend: `docker compose logs -f backend`
- Проверка доступности от Caddy:
  - `docker compose exec caddy sh -lc 'wget -S -O- -T 3 http://frontend:80/'`
  - `docker compose exec caddy sh -lc 'wget -S -O- -T 3 http://backend:8080/'`
- Порты на хосте: `ss -ltnp | egrep ':22|:80|:443'`
- UFW: `sudo ufw status numbered`

### Частые проблемы
- 502 через Caddy:
  - Неверный порт фронта (должно быть `frontend:80`, если nginx).
  - Бэкенд не слушает `:8080` или упал — см. логи backend.
  - Ошибки БД: неверный пароль/нет БД. Решение:
    - Синхронизировать `POSTGRES_PASSWORD` и `DB_PASSWORD`.
    - Создать БД: `CREATE DATABASE portfolio_db OWNER portfolio_user;`.
- Caddyfile не грузится:
  - Только поддерживаемые директивы. Любые пояснения — через `#`.

---

## Усиление безопасности (рекомендуется)

- SSH:
  - Вход по ключам; запрет паролей: в `/etc/ssh/sshd_config` задать `PermitRootLogin no`, `PasswordAuthentication no`; затем `sudo systemctl reload sshd`.
- Fail2ban и автообновления:
  - `sudo apt install -y fail2ban unattended-upgrades`
  - `echo -e "[sshd]\nenabled = true" | sudo tee /etc/fail2ban/jail.local`
  - `sudo systemctl restart fail2ban`
  - `sudo dpkg-reconfigure unattended-upgrades`
- Сильный пароль БД:
  - `docker compose exec db psql -U "$POSTGRES_USER" -d postgres -c "ALTER USER portfolio_user WITH PASSWORD 'СЛОЖНЫЙ_ПАРОЛЬ';"`
  - Обновить `backend/.env` → `DB_PASSWORD=...` и `docker compose restart backend`.

---

## Порты на роутере

- Текущий режим (HTTP‑only): пробросить только `80/TCP` → `192.168.1.100:80`.
- Публичный HTTPS (с доменом): пробросить `80/TCP` и `443/TCP` (опц. `443/UDP`).
- Не открывать наружу: `3000`, `8080`, `5432` — эти сервисы доступны только внутри Docker‑сети.

