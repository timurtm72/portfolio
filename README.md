# 🚀 Портфолио Тимура Султанова - Инженер-программист

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)
![React](https://img.shields.io/badge/React-18-blue)
![TypeScript](https://img.shields.io/badge/TypeScript-Latest-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue)

> Профессиональное портфолио инженера-программиста с 25+ летним опытом в области промышленной автоматизации, микроконтроллеров и ПЛК систем.

## 👨‍💻 О специалисте

**Султанов Тимур** - опытный инженер-программист из Иннополиса, Татарстан, с 8+ лет опыта в области:

- 🔧 **Микроконтроллеры**: STM32, ESP32, Texas Instruments Tiva, Atmel AVR, Nuvoton
- 🏭 **ПЛК и автоматизация**: Omron, ОВЕН, СПК107, JUMO, SCADA системы
- 🎨 **Проектирование**: Altium Designer, Easy EDA, схемотехника, силовая электроника  
- 🌐 **IoT и протоколы**: Modbus, Bluetooth, WiFi, GSM/SMS, 4-20mA
- 💻 **Разработка ПО**: Java Spring Boot, Android, C/C++, Python Django

## 📞 Контакты

- **Телефон**: [+7 927 244-40-51](tel:+79272444051)
- **Email**: [timursultanw@yandex.ru](mailto:timursultanw@yandex.ru)
- **Альтернативный email**: [timur.sultanov1972@gmail.com](mailto:timur.sultanov1972@gmail.com)
- **Местоположение**: Иннополис, Татарстан, Россия
- **GitHub**: [https://github.com/timurtm72](https://github.com/timurtm72)
- **YouTube**: [https://youtube.com/channel/UCGnkZF95JhfO7tGaB-oHC6A](https://youtube.com/channel/UCGnkZF95JhfO7tGaB-oHC6A)

## 🌟 Особенности портфолио

### ✨ Современный дизайн
- Адаптивный design с TailwindCSS
- Smooth анимации с Framer Motion
- Градиенты и современная типографика
- Темная/светлая тема

### 🎯 Специализированный контент
- 25+ навыков по категориям (микроконтроллеры, ПЛК, протоколы)
- 16+ реальных проектов промышленной автоматизации
- Детальные описания технических решений
- Ссылки на GitHub репозитории

### 🔧 Функциональность
- Фильтрация проектов по технологиям
- Поиск и сортировка
- Админ панель для управления контентом
- REST API для интеграций

## 🛠 Технологический стек

### Frontend
- **React 18** с TypeScript
- **TailwindCSS** для стилизации
- **Framer Motion** для анимаций
- **React Query** для state management
- **React Router** для навигации
- **Heroicons** для иконок

### Backend  
- **Spring Boot 3.5.3** (Java 17)
- **Spring Security** с JWT аутентификацией
- **Spring Data JPA** с Hibernate
- **PostgreSQL** база данных
- **MapStruct** для mapping
- **Lombok** для clean code
- **Swagger/OpenAPI** документация

### Дополнительно
- **Maven** для сборки
- **pgAdmin** для управления БД
- **Windows PowerShell** скрипты
- Готовые SQL данные

## 🚀 Быстрый старт

### Предварительные требования
- Java 17+
- Node.js 18+
- PostgreSQL 12+
- Maven 3.8+

### 1. Клонирование и настройка

```bash
git clone <repository-url>
cd Site
```

### 2. Настройка базы данных

```sql
-- Подключитесь к PostgreSQL как superuser
psql -U postgres

-- Выполните скрипт создания пользователя и БД
\i database/create-user-and-db.sql

-- Подключитесь к новой БД и создайте схему
\c portfolio_db
\i database/schema.sql

-- Загрузите реальные данные
\i database/real-portfolio-data.sql
```

### 3. Запуск Backend

```bash
cd backend
mvn spring-boot:run
```

Backend будет доступен на: http://localhost:8080/api

### 4. Запуск Frontend

```bash
cd frontend
npm install
npm start
```

Frontend будет доступен на: http://localhost:3000

## 📂 Структура проекта

```
Site/
├── backend/                 # Spring Boot приложение
│   ├── src/main/java/      # Java код
│   ├── src/main/resources/ # Конфигурация
│   └── pom.xml            # Maven зависимости
├── frontend/               # React приложение
│   ├── src/               # React компоненты
│   ├── public/            # Статические файлы
│   └── package.json       # NPM зависимости
├── database/              # SQL скрипты
│   ├── schema.sql         # Схема БД
│   ├── real-portfolio-data.sql # Реальные данные
│   └── LOAD_REAL_DATA.md  # Инструкции
└── README.md
```

## 📊 Основные проекты (в портфолио)

### 🔌 IoT и микроконтроллеры
- **USB Bluetooth Mass Storage** для швейных машин Janome
- **Сигнализатор утечки газа** (автомобильный и домашний)  
- **LED контроллеры** с Modbus и Firebase
- **Умный дом** на Python Django + ESP32

### 🏭 Промышленная автоматизация
- **Система управления инкубатором** (Texas Instruments Tiva)
- **Управление насосами** с датчиками 4-20mA
- **Стабилизатор напряжения 20кВт** на STM32L152
- **Климат-контроль** ОВЕН PLC160 + SCADA

### 🔧 Силовая электроника и ПЛК
- **Щиты управления** насосными станциями
- **Системы контроля доступа** Altium Designer
- **Проекты нефтепереработки** на JUMO ПЛК
- **Блоки питания** и преобразователи

## 🔐 Админ панель

Доступна по адресу: http://localhost:3000/admin

**Логин**: admin  
**Пароль**: admin123

### Возможности админки:
- ✅ Управление проектами (CRUD)
- ✅ Управление навыками
- ✅ Загрузка изображений
- ✅ Статистика и аналитика
- ✅ Пользователи и роли

## 🌐 API документация

После запуска backend, документация доступна по адресу:
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

### Основные endpoints:
- `GET /api/projects` - Получить все проекты
- `GET /api/skills` - Получить все навыки
- `POST /api/auth/login` - Аутентификация
- `GET /api/projects/{id}` - Получить проект по ID

## 🔧 Разработка

### Команды для разработки

```bash
# Запуск в dev режиме
npm run dev          # Frontend
mvn spring-boot:run  # Backend

# Сборка для продакшена
npm run build        # Frontend
mvn clean package    # Backend

# Тестирование
npm test            # Frontend тесты
mvn test           # Backend тесты
```

### Переменные окружения

#### Backend (`application.yml`)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/portfolio_db
    username: portfolio_user
    password: portfolio_password
```

#### Frontend (`.env`)
```env
REACT_APP_API_URL=http://localhost:8080/api
```

## 📝 TODO / Будущие улучшения

- [ ] Добавить реальные изображения проектов
- [ ] Интеграция с GitHub API для актуальных репозиториев
- [ ] Блог/статьи о технических решениях
- [ ] Многоязычность (EN/RU)
- [ ] PWA функциональность
- [ ] Docker контейнеризация
- [ ] CI/CD pipeline
- [ ] Метрики и аналитика

## 🤝 Вклад в проект

1. Fork репозитория
2. Создайте feature branch (`git checkout -b feature/amazing-feature`)
3. Commit изменения (`git commit -m 'Add amazing feature'`)
4. Push в branch (`git push origin feature/amazing-feature`)
5. Откройте Pull Request

## 📄 Лицензия

Этот проект является портфолио Тимура Султанова. Код может использоваться как пример или основа для собственных проектов с указанием авторства.

## 🔗 Полезные ссылки

- [Основной сайт портфолио](https://technocom.site123.me/)
- [GitHub с проектами](https://github.com/timurtm72)
- [YouTube канал](https://youtube.com/channel/UCGnkZF95JhfO7tGaB-oHC6A)
- [Spring Boot документация](https://spring.io/projects/spring-boot)
- [React документация](https://reactjs.org/)
- [TailwindCSS](https://tailwindcss.com/)

## 🖼 Управление изображениями проектов и галерея

Новая версия портфолио поддерживает **несколько изображений для одного проекта** и полноэкранный зум-просмотр.

1. Размещайте файлы в папке
   `frontend/public/images/projects/webp/`  
   (при сборке они доступны по URL `/images/projects/webp/<filename>.webp`).
2. В **DataLoader** или через Admin API укажите:
   - `imageUrl` – основное изображение (миниатюра).
   - `images` – набор путей (Set<String>) для галереи.  
     Пример для Java:
   ```java
   new HashSet<>(Arrays.asList(
       "/images/projects/webp/janome.webp",
       "/images/projects/webp/janome1.webp",
       "/images/projects/webp/janome2.webp"))
   ```
3. Если поле `images` пустое, фронтенд **автоматически** попытается найти файлы с тем же
   именем и суффиксами `1.webp`, `2.webp`, `3.webp` и отобразит их.
4. Во фронтенде компонент `ProjectModal` показывает:
   - крупное фото;
   - полосу миниатюр;
   - зум-оверлей с навигацией «‹ / ›».  
   Клик вне изображения — выход из зума.

> 💡 Изображения WebP весят меньше, поэтому предпочтительно использовать именно этот формат.

---

## 🗑 Очистка таблицы проектов (reset)

При изменениях схемы или массовом обновлении данных удобно удалить старые записи и
сбросить sequence.

### Вариант 1. SQL-скрипт

```sql
\i database/clear-all-projects.sql
```

### Вариант 2. REST endpoint (требует роль **ADMIN**)

```bash
curl -X DELETE "http://localhost:8080/api/projects/admin/clear?resetSequence=true" \
     -u admin:admin123
```

Параметр `resetSequence=true` дополнительно выполняет
`ALTER SEQUENCE projects_id_seq RESTART WITH 1`.

После очистки перезапустите backend — **DataLoader** автоматически создаст
проекты заново, если их количество меньше 20.

---

**Создано с ❤️ для демонстрации экспертизы в области промышленной автоматизации и IoT разработки**

## 🌈 Изменения в интерфейсе

### Вернули светлую цветовую схему
- **index.css**: 
  - body снова `bg-gray-50 text-gray-900`.

- **Experience.tsx**: 
  - Градиентный фон, белые карточки + тени, светлый текст.
  - Светлые бейджи, иконки, summary-карточки.

- **Projects.tsx**: 
  - Фильтры и инпуты белые.
  - Карточки `bg-white shadow`.
  - Категории/статусы/featured-бейджи и technology-теги переведены на светлые (`…-100 text-…-800`).
  - Сообщения и счётчики теперь серый 600.

- **ProjectModal.tsx**: 
  - Основное изображение и превью на светлом фоне, светлые бордеры/тени.
  - Галерея без дублирования сохранена.

### Общие улучшения
- Обновлена палитра цветов во всех helper-функциях (`getCategoryColor`, `getStatusColor`) на светлую. 

## 📄 Инструкции по возврату светлой темы

Чтобы вернуть светлую цветовую схему в проекте, выполните следующие шаги:

1. **Измените файл `index.css`:**
   - Замените `bg-gray-900 text-gray-100` на `bg-gray-50 text-gray-900` в селекторе `body`.

2. **Обновите компоненты:**
   - В файле `Experience.tsx` установите градиентный фон и светлые карточки.
   - В файле `Projects.tsx` измените фон карточек на `bg-white` и обновите цвета бейджей на светлые.
   - В файле `ProjectModal.tsx` измените фон на светлый и обновите бордеры. 

## ⚙️ Конфигурация через переменные окружения

Для упрощения переноса приложения на любой сервер используется параметризация конфигурации. Ключевые переменные:

### Backend (Spring Boot)

| Переменная | По-умолчанию | Описание |
|------------|--------------|----------|
| `DB_HOST`  | `localhost`  | Хост PostgreSQL |
| `DB_PORT`  | `5432`       | Порт PostgreSQL |
| `DB_USER`  | `portfolio_user` | Пользователь БД |
| `DB_PASSWORD` | `portfolio_password` | Пароль БД |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:3000,http://localhost:3001` | Список доменов, которым разрешён доступ к API (через запятую) |

Эти переменные подставляются в `application.yml` с помощью синтаксиса `${VAR:default}`.

### Frontend (React)

Файлы-шаблоны уже добавлены в репозиторий:

* `backend/env.example`
* `frontend/env.example`

Скопируйте их под именем `.env` и при необходимости измените значения.

```bash
cp backend/env.example backend/.env
cp frontend/env.example frontend/.env
```

После этого в `frontend/.env` пропишите:

```env
REACT_APP_API_URL=http://localhost:8080/api
```

Компоненты React читают значение через `process.env.REACT_APP_API_URL`, что исключает жёсткие ссылки.

## 👶 Полное руководство для новичка (Docker-путь)

Пошаговый чек-лист для тех, кто **никогда** не поднимал веб-приложения.

### 0. Установите инструменты
1. **Git** – https://git-scm.com/downloads
2. **Docker Desktop** (Windows / macOS) либо `docker` и `docker-compose` из пакетного менеджера (Linux) – https://docs.docker.com/get-docker/
3. (Опционально) **Visual Studio Code** – удобно смотреть код и логи.

### 1. Склонируйте проект
```bash
git clone <repository-url> portfolio
cd portfolio
```

### 2. Подготовьте файлы переменных окружения
Скопируйте готовые образцы и при желании отредактируйте:

```bash
# Linux / macOS / Git Bash
cp backend/env.example backend/.env
cp frontend/env.example frontend/.env

git clone <repository-url> portfolio
cd portfolio
```

3. **Создание `.env` файлов**

```bash
# Backend env (./backend/.env)
cat > backend/.env <<EOF
DB_HOST=postgres
DB_PORT=5432
DB_USER=portfolio_user
DB_PASSWORD=superSecret
CORS_ALLOWED_ORIGINS=https://portfolio.yourdomain.com
EOF

# Frontend env (./frontend/.env)
echo "REACT_APP_API_URL=https://portfolio.yourdomain.com/api" > frontend/.env
```

4. **Docker Compose**

Создайте `docker-compose.yml` в корне (пример):

```yaml
version: '3.9'
services:
  db:
    image: postgres:16
    restart: unless-stopped
    environment:
      POSTGRES_USER: ${DB_USER:-portfolio_user}
      POSTGRES_PASSWORD: ${DB_PASSWORD:-portfolio_password}
      POSTGRES_DB: portfolio_db
    volumes:
      - db_data:/var/lib/postgresql/data

  backend:
    build: ./backend
    restart: unless-stopped
    env_file: ./backend/.env
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    ports:
      - "8080:8080"
    depends_on:
      - db

  frontend:
    build: ./frontend
    restart: unless-stopped
    env_file: ./frontend/.env
    ports:
      - "3000:80"   # production build обслуживается nginx/alpine внутри контейнера
    depends_on:
      - backend

volumes:
  db_data:
```

5. **Сборка и запуск**

```bash
docker compose build
docker compose up -d
```

Через пару минут приложения будут доступны по:

* **Frontend**: `https://portfolio.yourdomain.com` (порт 3000 при локальном тесте)
* **Backend API**: `https://portfolio.yourdomain.com/api`

6. **SSL / доменное имя**

Для HTTPS рекомендую поставить обратный прокси **Nginx** + `certbot`. Проксируйте 80/443 наружу, а внутрь — порты 3000 и 8080 контейнеров.

7. **Обновление приложения**

```bash
git pull
docker compose build frontend backend
docker compose up -d
```

## 📝 История изменений

- Добавлена поддержка переменных окружения для URL БД и списка CORS-доменов
- Удалены жёсткие ссылки `localhost` из кода (backend & frontend)
- Создан `frontend/src/config.ts` для базового URL API
- Обновлены инструкции по деплою и добавлены примеры `.env` и `docker-compose.yml` 