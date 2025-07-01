-- Создание пользователя и базы данных для портфолио
-- Выполнить в pgAdmin как супер-пользователь (postgres)

-- 1. Создать пользователя
CREATE USER portfolio_user WITH PASSWORD 'portfolio_password';

-- 2. Предоставить права на создание баз данных
ALTER USER portfolio_user CREATEDB;

-- 3. Создать базу данных
CREATE DATABASE portfolio_db WITH OWNER portfolio_user;

-- 4. Подключиться к новой базе данных
\c portfolio_db;

-- 5. Предоставить все права на схему public
GRANT ALL ON SCHEMA public TO portfolio_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO portfolio_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO portfolio_user;

-- 6. Установить права по умолчанию для будущих объектов
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO portfolio_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO portfolio_user;

-- Проверка созданного пользователя и базы данных
SELECT usename FROM pg_user WHERE usename = 'portfolio_user';
SELECT datname FROM pg_database WHERE datname = 'portfolio_db';

SELECT 'Пользователь и база данных успешно созданы!' as result; 