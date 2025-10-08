# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Архитектура проекта

Это fullstack приложение-портфолио инженера-программиста со следующей архитектурой:

- **Backend**: Spring Boot 3.5.3 (Java 17) + PostgreSQL
  - Структура: controller → service → repository → entity
  - Маппинг: MapStruct для DTO ↔ Entity преобразований
  - Безопасность: Spring Security с базовой аутентификацией (JWT поддержка добавлена)
  - База данных: PostgreSQL с JPA/Hibernate (`ddl-auto: update`)

- **Frontend**: React 18 + TypeScript + TailwindCSS
  - Роутинг: React Router v6
  - State management: React Query (@tanstack/react-query)
  - Стилизация: TailwindCSS + Framer Motion для анимаций
  - API клиент: Axios

- **База данных**: PostgreSQL
  - Основные сущности: `skills`, `projects`, `work_experience`, `users`
  - Автозагрузка данных: `DataLoader.java` проверяет наличие данных и загружает их при первом запуске

## Команды для разработки

### Backend (Spring Boot)
```bash
cd backend

# Запуск приложения
mvn spring-boot:run

# Сборка без тестов
mvn clean package -DskipTests

# Установка зависимостей
mvn clean install

# Запуск тестов
mvn test
```

Backend доступен: http://localhost:8080/api

### Frontend (React)
```bash
cd frontend

# Установка зависимостей
npm install

# Запуск dev сервера
npm start

# Сборка для продакшена
npm run build

# Тестирование
npm test
```

Frontend доступен: http://localhost:3000

### База данных (PostgreSQL)
```bash
# Подключение к БД
psql -U portfolio_user -d portfolio_db

# Применение схемы
psql -U portfolio_user -d portfolio_db -f database/schema.sql

# Загрузка реальных данных (если нужно)
psql -U portfolio_user -d portfolio_db -f database/real-portfolio-data.sql
```

## Переменные окружения

### Backend (application.yml)
- `DB_HOST` - хост PostgreSQL (по умолчанию: localhost)
- `DB_PORT` - порт PostgreSQL (по умолчанию: 5432)
- `DB_USER` - пользователь БД (по умолчанию: portfolio_user)
- `DB_PASSWORD` - пароль БД (по умолчанию: portfolio_password)
- `CORS_ALLOWED_ORIGINS` - список разрешенных CORS доменов (по умолчанию: http://localhost:3000,http://localhost:3001)

### Frontend (config.ts)
- `REACT_APP_API_URL` - URL бэкенда (по умолчанию: /api для относительных путей)

Файлы `.env` можно создать из `env.example` в соответствующих директориях.

## Важные особенности архитектуры

### Backend
1. **DataLoader.java** - автоматически загружает начальные данные при первом запуске
   - Проверяет количество проектов, навыков и опыта работы
   - Загружает данные только если их нет или мало (< 20 проектов)

2. **MapStruct маппинг** - генерация мапперов происходит на этапе компиляции
   - Процессоры аннотаций настроены в pom.xml
   - Связка Lombok + MapStruct работает через `lombok-mapstruct-binding`

3. **Spring Security** - публичные endpoints открыты, админские защищены
   - Публичные: `/api/skills/**`, `/api/projects/**`, `/api/work-experience/**`
   - Защищенные: `/api/admin/**`, `/api/auth/**`
   - CORS настроен через переменные окружения

4. **Hibernate ddl-auto: update** - схема БД обновляется автоматически, данные НЕ удаляются при перезапуске

### Frontend
1. **API клиент** - централизованная конфигурация в `config.ts`
   - Использует переменную окружения `REACT_APP_API_URL`
   - Fallback на `/api` для production сборок

2. **Структура компонентов**:
   - `pages/` - страницы приложения (Home, Projects, Experience, About, Contact)
   - `pages/Admin/` - админ панель (Login, Dashboard)
   - `components/` - переиспользуемые компоненты (ProjectModal, WebPImage)
   - `components/Layout/` - layout компоненты (Header, Footer)

3. **Изображения проектов** - поддержка WebP формата
   - Основные изображения: `/images/projects/webp/<name>.webp`
   - Галерея: автопоиск файлов `<name>1.webp`, `<name>2.webp`, `<name>3.webp`
   - Entity `Project` имеет поле `images` (Set<String>) для дополнительных фотографий

## API Endpoints

### Публичные
- `GET /api/skills` - получить все навыки
- `GET /api/projects` - получить все проекты
- `GET /api/work-experience` - получить опыт работы
- `GET /api/users/public/**` - публичная информация о пользователях

### Защищенные (требуют аутентификации)
- `POST /api/auth/login` - вход в систему
- Админ endpoints: `/api/admin/**`

### Документация API
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api/api-docs

### Админ панель
- URL: http://localhost:3000/admin
- Логин: `admin`
- Пароль: `admin123`

## Данные проекта

Портфолио содержит реальные данные Султанова Тимура:
- **Специализация**: микроконтроллеры (STM32, ESP32), ПЛК (Omron, JUMO), SCADA
- **Опыт**: 25+ лет в промышленной автоматизации
- **Проекты**: 16+ реальных проектов (IoT, промышленная автоматизация, силовая электроника)
- **Навыки**: 25+ навыков по категориям (микроконтроллеры, ПЛК, протоколы, программирование)

## Особенности разработки

### При работе с Backend:
- Все маппинг-классы генерируются автоматически Maven при компиляции
- При изменении Entity/DTO нужно пересобрать проект: `mvn clean compile`
- Используйте Lombok для сокращения boilerplate кода
- Логирование настроено через Slf4j + Logback

### При работе с Frontend:
- Все общение на русском языке, включая комментарии
- Используйте TypeScript для типобезопасности
- Следуйте best practices React и TailwindCSS
- Анимации через Framer Motion

### При работе с БД:
- DataLoader проверяет данные при каждом запуске
- Для полной очистки проектов есть endpoint: `DELETE /api/projects/admin/clear?resetSequence=true`
- SQL скрипты в директории `database/`

## Структура Java пакетов

```
com.example.backend/
├── BackendApplication.java       # Точка входа Spring Boot
├── DataLoader.java                # Автозагрузка начальных данных
├── config/                        # Конфигурация (SecurityConfig)
├── controller/                    # REST контроллеры
├── dto/                          # Data Transfer Objects
├── entity/                       # JPA сущности
├── mapper/                       # MapStruct маппинг интерфейсы
├── repository/                   # Spring Data JPA репозитории
└── service/                      # Бизнес-логика
```

## Языковые требования

- **Все общение на русском языке**
- **Комментарии в коде на русском языке**
- Если не знаете правильного ответа - скажите об этом, не придумывайте

## Production сервер и деплой

Проект развернут на production сервере:
- **IP**: 103.106.3.98
- **Расположение**: `/opt/portfolio`
- **OS**: Ubuntu 24.04
- **Сервисы**:
  - Backend: systemd unit `portfolio-backend.service`
  - Frontend: Nginx с root `/var/www/portfolio`
  - Прокси: Nginx → `127.0.0.1:8080` (backend API)

### Деплой обновлений

На сервере есть скрипт `/opt/portfolio/run-deploy.sh`:

```bash
# Запуск деплоя с локальной машины
ssh root@103.106.3.98 'bash /opt/portfolio/run-deploy.sh'
```

Скрипт выполняет:
1. `git pull` последних изменений из GitHub
2. Сборку backend: `./mvnw clean package -DskipTests`
3. Сборку frontend: `npm ci && npm run build`
4. Копирование статики в `/var/www/portfolio/`
5. Рестарт сервисов: `systemctl restart portfolio-backend` + `nginx -s reload`

Подробности в [CODING_WORKFLOW.md](CODING_WORKFLOW.md)

### Проверка деплоя

```bash
# API health check
curl -I http://103.106.3.98/api/health

# Frontend
curl -IL http://technocom.site123.me
```

## Docker деплой (альтернатива)

Проект поддерживает Docker с оптимизацией для ARM64 (Orange Pi):

### Быстрый старт:
```bash
# Создать .env файлы
cp backend/env.example backend/.env
cp frontend/env.example frontend/.env

# Запуск
docker compose build
docker compose up -d
```

### Команды управления Docker:
```bash
# Просмотр статуса
docker compose ps

# Просмотр логов
docker compose logs -f
docker compose logs -f backend
docker compose logs -f frontend

# Остановка/запуск
docker compose stop
docker compose start
docker compose restart

# Полная остановка и удаление
docker compose down

# Пересборка после изменений
docker compose build
docker compose up -d

# Мониторинг ресурсов
docker stats
```

### Особенности Docker setup:
- **Multi-stage build** для оптимизации размера образов
- **Healthcheck** для всех сервисов (PostgreSQL, Backend)
- **Автоматическое применение схемы БД** при первом запуске
- **Alpine образы** для минимального размера
- **Сеть `portfolio-network`** для изоляции контейнеров

Подробная инструкция в [README.md](README.md) (раздел "Полное руководство для новичка").

## База данных - SQL скрипты

В директории `database/` находятся важные SQL скрипты:

- `schema.sql` - схема базы данных
- `create-user-and-db.sql` - создание пользователя и БД
- `real-portfolio-data.sql` - реальные данные портфолио (16+ проектов, 25+ навыков)
- `clear-all-projects.sql` - полная очистка таблицы проектов
- `update-image-paths.sql` - обновление путей к изображениям

## GitHub репозиторий

- **URL**: https://github.com/timurtm72/portfolio
- **Основная ветка**: `main`
- **Workflow**: Feature branches → Pull Request → Squash & Merge
- **Commit style**: Conventional Commits (`feat:`, `fix:`, `docs:`, etc.)

## Деплой на Orange Pi Zero 3

Проект может быть развернут на Orange Pi Zero 3 (4GB RAM) через Docker:

### Характеристики Orange Pi Zero 3:
- **CPU**: AllWinner H618 (Quad-core ARM Cortex-A53 @ 1.5GHz)
- **RAM**: 1GB / 1.5GB / 2GB / 4GB LPDDR4
- **Архитектура**: ARM64 (aarch64)
- **Сеть**: Gigabit Ethernet, WiFi 6

### Быстрый старт:

```bash
# На Orange Pi
git clone https://github.com/timurtm72/portfolio.git
cd portfolio

# Создать .env файлы
cp backend/env.example backend/.env
cp frontend/env.example frontend/.env

# Запустить через Docker
docker compose build
docker compose up -d
```

**Полная инструкция**: [deploy/SETUP-ORANGEPI-ZERO3.html](deploy/SETUP-ORANGEPI-ZERO3.html)

Инструкция включает:
- Установку Docker на Debian 11
- **Настройку WiFi приоритета** и автоподключение к нескольким сетям
- Настройку проброса портов на роутере
- Команды управления контейнерами
- Решение типичных проблем
- Мониторинг и безопасность

### Особенности для Orange Pi:
- **WiFi в приоритете над Ethernet**: настройка метрик маршрутизации
- **Автоподключение к нескольким WiFi**: поддержка разных локаций (дом, работа)
- **Сохранение настроек после перезагрузки**: все конфигурации постоянные
- **Оптимизация для ARM64**: Docker образы собираются под архитектуру ARM
- **Время сборки**: ~30-60 минут на Orange Pi Zero 3 при первом запуске
