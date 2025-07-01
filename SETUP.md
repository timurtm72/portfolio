# Настройка окружения разработки

## Установка необходимых инструментов

### 1. Java Development Kit (JDK)

**Скачать и установить OpenJDK 17:**
- Перейдите на https://adoptium.net/
- Скачайте OpenJDK 17 для Windows
- Установите с настройками по умолчанию
- Убедитесь, что переменная `JAVA_HOME` настроена

**Проверка установки:**
```bash
java -version
```

### 2. Apache Maven

**Скачать и установить Maven:**
- Перейдите на https://maven.apache.org/download.cgi
- Скачайте Binary zip archive (например, `apache-maven-3.9.5-bin.zip`)
- Извлеките в `C:\Program Files\Apache\Maven`
- Добавьте `C:\Program Files\Apache\Maven\bin` в переменную PATH

**Проверка установки:**
```bash
mvn -version
```

### 3. PostgreSQL

**Скачать и установить PostgreSQL:**
- Перейдите на https://www.postgresql.org/download/windows/
- Скачайте PostgreSQL 15 или новее
- При установке запомните пароль для пользователя `postgres`
- Убедитесь, что PostgreSQL сервис запущен

**Проверка установки:**
```bash
psql --version
```

### 4. Node.js

**Скачать и установить Node.js:**
- Перейдите на https://nodejs.org/
- Скачайте LTS версию (18.x или новее)
- Установите с настройками по умолчанию

**Проверка установки:**
```bash
node --version
npm --version
```

## Настройка базы данных

### 1. Создание базы данных

Откройте командную строку PostgreSQL (psql) как администратор базы данных:

```sql
-- Подключение к PostgreSQL
psql -U postgres

-- Создание базы данных
CREATE DATABASE portfolio_db;

-- Создание пользователя
CREATE USER portfolio_user WITH PASSWORD 'portfolio_password';

-- Предоставление прав
GRANT ALL PRIVILEGES ON DATABASE portfolio_db TO portfolio_user;

-- Подключение к новой базе данных
\c portfolio_db;

-- Предоставление дополнительных прав
GRANT ALL ON SCHEMA public TO portfolio_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO portfolio_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO portfolio_user;

-- Выход
\q
```

### 2. Применение схемы

Выполните SQL схему из файла `database/schema.sql`:

```bash
psql -U portfolio_user -d portfolio_db -f database/schema.sql
```

## Настройка проекта

### 1. Клонирование и настройка

```bash
# Перейдите в директорию проекта
cd C:\Users\{ваше_имя}\Desktop\Site

# Убедитесь, что все файлы на месте
dir
```

### 2. Настройка Backend

```bash
cd backend

# Проверьте настройки в src/main/resources/application.yml
# При необходимости измените параметры подключения к БД

# Установка зависимостей
mvn clean install

# Запуск приложения
mvn spring-boot:run
```

Backend будет доступен по адресу: http://localhost:8080

### 3. Настройка Frontend

Откройте новое окно командной строки:

```bash
cd frontend

# Установка зависимостей (уже выполнено)
npm install

# Запуск в режиме разработки
npm start
```

Frontend будет доступен по адресу: http://localhost:3000

## Проверка работы

### 1. Проверка Backend API

Откройте в браузере:
- http://localhost:8080/api/projects - должен вернуть JSON с проектами
- http://localhost:8080/swagger-ui.html - документация API

### 2. Проверка Frontend

Откройте в браузере:
- http://localhost:3000 - главная страница портфолио

### 3. Проверка админ панели

- http://localhost:3000/admin - страница входа в админ панель

## Возможные проблемы и решения

### Maven не найден
```bash
# Убедитесь, что Maven добавлен в PATH
echo $env:PATH

# Или используйте полный путь
C:\Program Files\Apache\Maven\bin\mvn clean install
```

### Ошибка подключения к базе данных
1. Убедитесь, что PostgreSQL запущен
2. Проверьте настройки в `application.yml`
3. Убедитесь, что база данных и пользователь созданы

### Порт уже используется
Если порт 8080 или 3000 занят, измените настройки:
- Backend: `server.port` в `application.yml`
- Frontend: используйте другой порт через переменную `PORT=3001 npm start`

### Ошибки компиляции TypeScript
```bash
cd frontend
npm install --legacy-peer-deps
```

## Полезные команды

### Backend
```bash
# Сборка без тестов
mvn clean package -DskipTests

# Запуск с профилем
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Просмотр логов
tail -f logs/application.log
```

### Frontend
```bash
# Сборка для продакшена
npm run build

# Линтинг кода
npm run lint

# Тестирование
npm test
```

### База данных
```bash
# Подключение к БД
psql -U portfolio_user -d portfolio_db

# Backup базы данных
pg_dump -U portfolio_user portfolio_db > backup.sql

# Восстановление
psql -U portfolio_user -d portfolio_db < backup.sql
```

## Следующие шаги

1. ✅ Установите все необходимые инструменты
2. ✅ Создайте и настройте базу данных
3. ✅ Запустите backend приложение
4. ✅ Запустите frontend приложение
5. 🔄 Протестируйте функциональность
6. 🔄 Добавьте свои проекты через админ панель
7. 🔄 Кастомизируйте дизайн под ваши потребности

## Контакты

Если у вас возникли проблемы с настройкой, создайте issue в репозитории проекта. 