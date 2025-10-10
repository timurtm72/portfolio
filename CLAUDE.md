# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Архитектура проекта

Fullstack портфолио инженера-программиста Тимура Султанова (25+ лет опыта в промышленной автоматизации, микроконтроллерах STM32/ESP32, ПЛК).

### Stack
- **Backend**: Spring Boot 3.5.3 (Java 17) + PostgreSQL
  - Архитектура: controller → service → repository → entity
  - Маппинг: MapStruct для DTO ↔ Entity
  - Безопасность: Spring Security с JWT
  - ORM: JPA/Hibernate (`ddl-auto: update` - данные НЕ удаляются при перезапуске)

- **Frontend**: React 18 + TypeScript + TailwindCSS
  - Роутинг: React Router v6
  - State: React Query (@tanstack/react-query)
  - Анимации: Framer Motion
  - HTTP: Axios

- **База данных**: PostgreSQL
  - Сущности: `skills`, `projects`, `work_experience`, `users`
  - Автозагрузка: `DataLoader.java` проверяет данные и загружает при первом запуске (< 20 проектов)

## Команды для разработки

### Backend (Spring Boot)
```bash
cd backend

# Запуск в dev режиме
mvn spring-boot:run

# Сборка (пропуск тестов)
mvn clean package -DskipTests

# Компиляция MapStruct мапперов (после изменения Entity/DTO)
mvn clean compile

# Тесты
mvn test
```

**URL:** http://localhost:8080/api

### Frontend (React)
```bash
cd frontend

# Установка зависимостей
npm install

# Dev сервер
npm start

# Production сборка
npm run build

# Тесты
npm test
```

**URL:** http://localhost:3000

### База данных (PostgreSQL)
```bash
# Подключение
psql -U portfolio_user -d portfolio_db

# Применение схемы
psql -U portfolio_user -d portfolio_db -f database/schema.sql

# Загрузка данных
psql -U portfolio_user -d portfolio_db -f database/real-portfolio-data.sql
```

## Переменные окружения

Файлы `.env` создаются из `env.example`:
```bash
cp backend/env.example backend/.env
cp frontend/env.example frontend/.env
```

### Backend (application.yml)
- `DB_HOST` - хост PostgreSQL (default: `localhost`)
- `DB_PORT` - порт PostgreSQL (default: `5432`)
- `DB_USER` - пользователь БД (default: `portfolio_user`)
- `DB_PASSWORD` - пароль БД (default: `portfolio_password`)
- `CORS_ALLOWED_ORIGINS` - разрешенные домены (default: `http://localhost:3000,http://localhost:3001`)

### Frontend (config.ts)
- `REACT_APP_API_URL` - URL бэкенда (default: `/api` для относительных путей)

## Важные особенности

### Backend
1. **DataLoader.java** - автозагрузка начальных данных
   - Запускается при старте приложения
   - Загружает данные только если проектов < 20
   - Не дублирует существующие записи

2. **MapStruct маппинг** - автогенерация мапперов при компиляции
   - Процессоры аннотаций в `pom.xml`
   - Связка Lombok + MapStruct через `lombok-mapstruct-binding`
   - После изменения Entity/DTO: `mvn clean compile`

3. **Spring Security** - защита endpoints
   - Публичные: `/api/skills/**`, `/api/projects/**`, `/api/work-experience/**`
   - Защищенные: `/api/admin/**`, `/api/auth/**`
   - CORS настроен через переменные окружения

4. **Hibernate ddl-auto: update** - схема обновляется автоматически, данные сохраняются

### Frontend
1. **API клиент** - централизованная конфигурация `config.ts`
   - Использует `REACT_APP_API_URL` из `.env`
   - Fallback на `/api` для production

2. **Структура компонентов**:
   - `pages/` - Home, Projects, Experience, About, Contact
   - `pages/Admin/` - Login, Dashboard
   - `components/` - ProjectModal, WebPImage
   - `components/Layout/` - Header, Footer

3. **Изображения проектов** - поддержка WebP
   - Основное: `/images/projects/webp/<name>.webp`
   - Галерея: автопоиск `<name>1.webp`, `<name>2.webp`, `<name>3.webp`
   - Entity `Project` имеет поле `images` (Set<String>)

## API Endpoints

### Публичные
- `GET /api/skills` - все навыки
- `GET /api/projects` - все проекты
- `GET /api/work-experience` - опыт работы
- `GET /api/users/public/**` - публичная информация

### Защищенные (требуют аутентификации)
- `POST /api/auth/login` - вход в систему
- `/api/admin/**` - админ операции

### Документация API
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api/api-docs

### Админ панель
- URL: http://localhost:3000/admin
- Логин: `admin`
- Пароль: `admin123`

## Данные проекта

Портфолио содержит **реальные данные** Султанова Тимура:
- **Специализация**: микроконтроллеры (STM32, ESP32), ПЛК (Omron, JUMO), SCADA
- **Опыт**: 25+ лет в промышленной автоматизации
- **Проекты**: 16+ реальных проектов (IoT, промышленная автоматизация, силовая электроника)
- **Навыки**: 25+ навыков по категориям

## Структура Java пакетов

```
com.example.backend/
├── BackendApplication.java       # Точка входа Spring Boot
├── DataLoader.java                # Автозагрузка начальных данных
├── config/                        # SecurityConfig, CorsConfig
├── controller/                    # REST контроллеры
├── dto/                          # Data Transfer Objects
├── entity/                       # JPA сущности
├── mapper/                       # MapStruct интерфейсы
├── repository/                   # Spring Data JPA
└── service/                      # Бизнес-логика
```

## Языковые требования

- **Все общение на русском языке**
- **Комментарии в коде на русском языке**
- Если не знаете правильного ответа - скажите об этом, не придумывайте

## Production сервер

Развернут на VPS сервере:
- **IP**: 103.106.3.98
- **Расположение**: `/opt/portfolio`
- **OS**: Ubuntu 24.04
- **Сервисы**:
  - Backend: systemd unit `portfolio-backend.service`
  - Frontend: Nginx `/var/www/portfolio`
  - Прокси: Nginx → `127.0.0.1:8080`

### Деплой обновлений

Скрипт `/opt/portfolio/run-deploy.sh`:
```bash
ssh root@103.106.3.98 'bash /opt/portfolio/run-deploy.sh'
```

Выполняет:
1. `git pull` из GitHub
2. Сборку backend: `./mvnw clean package -DskipTests`
3. Сборку frontend: `npm ci && npm run build`
4. Копирование статики в `/var/www/portfolio/`
5. Рестарт: `systemctl restart portfolio-backend && nginx -s reload`

## Docker деплой

Проект поддерживает Docker с оптимизацией для ARM64 (Orange Pi).

### Быстрый старт
```bash
# Создать .env файлы
cp backend/env.example backend/.env
cp frontend/env.example frontend/.env

# Запуск
docker compose build
docker compose up -d
```

### Управление Docker
```bash
# Статус
docker compose ps

# Логи
docker compose logs -f
docker compose logs -f backend
docker compose logs -f frontend

# Управление
docker compose stop
docker compose start
docker compose restart
docker compose down

# Пересборка
docker compose build
docker compose up -d

# Мониторинг
docker stats
```

### Особенности Docker setup
- Multi-stage build для оптимизации размера
- Healthcheck для всех сервисов
- Автоматическое применение схемы БД
- Alpine образы для минимального размера (где возможно)
- Изолированная сеть `portfolio-network`

### ARM64 специфика (Orange Pi, Raspberry Pi)

**Важно:** Стандартные Docker образы могут не работать на ARM64 архитектуре.

#### Backend Dockerfile (ARM64-совместимый)
```dockerfile
# Используйте Amazon Corretto вместо Eclipse Temurin
FROM amazoncorretto:17-alpine AS build
# ... сборка ...
FROM amazoncorretto:17-alpine
# ... runtime ...
```

**Проблема Eclipse Temurin на ARM64:**
```
failed to resolve source metadata for eclipse-temurin:17-jre-alpine:
no match for platform in manifest: not found
```

**Решение:** Amazon Corretto имеет полную поддержку ARM64.

#### Frontend Dockerfile (ARM64-совместимый)
```dockerfile
# Используйте полный Debian образ вместо Alpine
FROM node:18 AS build  # НЕ node:18-alpine!
RUN npm install        # НЕ npm ci --only=production!
# ... остальное ...
```

**Проблема Alpine + ARM64:**
- Alpine использует musl libc вместо glibc
- Некоторые npm пакеты (включая `react-scripts`) имеют проблемы совместимости
- `npm ci --only=production` не устанавливает dev-зависимости, необходимые для сборки

**Решение:**
1. Используйте `node:18` (Debian) для build stage
2. Используйте `npm install` вместо `npm ci --only=production`
3. Финальный образ остается `nginx:alpine` (статика не зависит от платформы)

#### Порты в docker-compose.yml
```yaml
frontend:
  ports:
    - "3000:80"  # Внешний порт 3000, внутренний 80 (nginx)
```

**Примечание:** Frontend доступен на порту 3000, а не 80!

## База данных - SQL скрипты

Директория `database/`:
- `schema.sql` - схема БД
- `create-user-and-db.sql` - создание пользователя и БД
- `real-portfolio-data.sql` - реальные данные (16+ проектов, 25+ навыков)
- `clear-all-projects.sql` - полная очистка таблицы проектов
- `update-image-paths.sql` - обновление путей к изображениям

## GitHub репозиторий

- **URL**: https://github.com/timurtm72/portfolio
- **Ветка**: `main`
- **Workflow**: Feature branches → Pull Request → Squash & Merge
- **Commits**: Conventional Commits (`feat:`, `fix:`, `docs:`)

### Клонирование
```bash
# Публичный репозиторий
git clone https://github.com/timurtm72/portfolio.git

# Приватный - используйте Personal Access Token
git clone https://TOKEN@github.com/timurtm72/portfolio.git
```

**Примечание:** GitHub не поддерживает пароль для HTTPS. Используйте Personal Access Token или SSH.

## Деплой на Orange Pi Zero 3

Поддерживается развертывание на Orange Pi Zero 3 через Docker.

### Характеристики
- **CPU**: AllWinner H618 (Quad-core ARM Cortex-A53 @ 1.5GHz)
- **RAM**: 1GB / 1.5GB / 2GB / 4GB LPDDR4
- **Архитектура**: ARM64 (aarch64)
- **Сеть**: Gigabit Ethernet, WiFi 6

### Быстрый старт
```bash
git clone https://github.com/timurtm72/portfolio.git
cd portfolio

cp backend/env.example backend/.env
cp frontend/env.example frontend/.env

docker compose build  # 30-60 минут
docker compose up -d
```

**Полная инструкция**: [deploy/SETUP-ORANGEPI-ZERO3.html](deploy/SETUP-ORANGEPI-ZERO3.html)

### WiFi настройка с приоритетом над Ethernet

**Критично для Orange Pi**: WiFi должен иметь приоритет над Ethernet для мобильности между локациями.

Скрипт автонастройки: [deploy/SSH-WIFI-SETUP.md](deploy/SSH-WIFI-SETUP.md)

Быстрая команда (через SSH):
```bash
cd ~
# Скопировать скрипт из SSH-WIFI-SETUP.md
chmod +x wifi-priority-setup.sh
sudo ./wifi-priority-setup.sh
```

**Ключевые параметры:**
- WiFi: `autoconnect-priority=100`, `route-metric=100` (высокий приоритет)
- Ethernet: `autoconnect-priority=-10`, `route-metric=1000` (низкий приоритет)

**Проверка работы:**
```bash
ip route show  # WiFi (metric 100) должен быть первым
nmcli -f NAME,AUTOCONNECT-PRIORITY connection show
```

Поддержка двух WiFi сетей (например, дом и работа) с автоматическим переключением.

### Особенности для Orange Pi
- **WiFi приоритет**: метрики маршрутизации (`route-metric`) критичны
- **Автоподключение к нескольким WiFi**: поддержка разных локаций
- **Сохранение настроек**: все конфигурации постоянные
- **Оптимизация для ARM64**: образы собираются под ARM
- **Время сборки**: 30-60 минут при первом запуске

## Особенности разработки

### Backend
- Маппинг-классы генерируются Maven при компиляции
- После изменения Entity/DTO: `mvn clean compile`
- Используйте Lombok для сокращения boilerplate
- Логирование: Slf4j + Logback

### Frontend
- Используйте TypeScript для типобезопасности
- Следуйте React best practices
- TailwindCSS для стилизации
- Анимации через Framer Motion

### База данных
- DataLoader проверяет данные при каждом запуске
- Для очистки проектов: `DELETE /api/projects/admin/clear?resetSequence=true`
- SQL скрипты в `database/`

## Управление изображениями проектов

Поддержка нескольких изображений для одного проекта:

1. Размещайте файлы в `frontend/public/images/projects/webp/`
2. В DataLoader или Admin API:
   - `imageUrl` - основное изображение
   - `images` - Set<String> для галереи
   ```java
   new HashSet<>(Arrays.asList(
       "/images/projects/webp/janome.webp",
       "/images/projects/webp/janome1.webp",
       "/images/projects/webp/janome2.webp"))
   ```
3. Если `images` пустое - фронтенд автоматически ищет `<name>1.webp`, `<name>2.webp`, `<name>3.webp`
4. Компонент `ProjectModal` показывает галерею с зумом

💡 Используйте WebP формат для меньшего размера файлов.

## Очистка таблицы проектов

### SQL скрипт
```bash
psql -U portfolio_user -d portfolio_db -f database/clear-all-projects.sql
```

### REST endpoint (требует роль ADMIN)
```bash
curl -X DELETE "http://localhost:8080/api/projects/admin/clear?resetSequence=true" \
     -u admin:admin123
```

Параметр `resetSequence=true` выполняет `ALTER SEQUENCE projects_id_seq RESTART WITH 1`.

После очистки перезапустите backend - DataLoader создаст проекты заново.
