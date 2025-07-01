-- PostgreSQL схема для портфолио
-- Создание базы данных и пользователя

-- Создать базу данных (выполнить как суперпользователь)
CREATE DATABASE portfolio_db;

-- Создать пользователя
CREATE USER portfolio_user WITH PASSWORD 'portfolio_password';

-- Предоставить права пользователю
GRANT ALL PRIVILEGES ON DATABASE portfolio_db TO portfolio_user;

-- Подключиться к базе данных portfolio_db
\c portfolio_db;

-- Создать схему public (если не существует)
CREATE SCHEMA IF NOT EXISTS public;

-- Предоставить права на схему
GRANT ALL ON SCHEMA public TO portfolio_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO portfolio_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO portfolio_user;

-- Изменить права по умолчанию
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO portfolio_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO portfolio_user;

-- Таблица пользователей
CREATE TABLE public.users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    enabled BOOLEAN DEFAULT TRUE,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE public.users IS 'Пользователи системы для админ панели';

-- Таблица проектов
CREATE TABLE public.projects (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    short_description VARCHAR(500),
    github_url VARCHAR(500),
    live_url VARCHAR(500),
    video_url VARCHAR(500),
    category VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
    display_order INTEGER DEFAULT 0,
    featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE public.projects IS 'Проекты портфолио инженера-программиста';

-- Таблица технологий проектов
CREATE TABLE public.project_technologies (
    project_id BIGINT NOT NULL,
    technology VARCHAR(100) NOT NULL,
    FOREIGN KEY (project_id) REFERENCES public.projects(id) ON DELETE CASCADE,
    PRIMARY KEY (project_id, technology)
);
COMMENT ON TABLE public.project_technologies IS 'Технологии, используемые в проектах';

-- Таблица изображений проектов
CREATE TABLE public.project_images (
    project_id BIGINT NOT NULL,
    image_path VARCHAR(500) NOT NULL,
    FOREIGN KEY (project_id) REFERENCES public.projects(id) ON DELETE CASCADE,
    PRIMARY KEY (project_id, image_path)
);
COMMENT ON TABLE public.project_images IS 'Изображения проектов';

-- Таблица навыков
CREATE TABLE public.skills (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    level INTEGER NOT NULL CHECK (level >= 1 AND level <= 100),
    category VARCHAR(50) NOT NULL,
    icon_path VARCHAR(500),
    color VARCHAR(20),
    display_order INTEGER DEFAULT 0,
    years_of_experience INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE public.skills IS 'Навыки и технологии инженера-программиста';

-- Индексы для лучшей производительности
CREATE INDEX idx_projects_category ON public.projects(category);
CREATE INDEX idx_projects_status ON public.projects(status);
CREATE INDEX idx_projects_featured ON public.projects(featured);
CREATE INDEX idx_projects_display_order ON public.projects(display_order);
CREATE INDEX idx_skills_category ON public.skills(category);
CREATE INDEX idx_skills_level ON public.skills(level);
CREATE INDEX idx_skills_display_order ON public.skills(display_order);
CREATE INDEX idx_users_username ON public.users(username);
CREATE INDEX idx_users_email ON public.users(email);

-- Триггер для автоматического обновления updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$ language 'plpgsql';

-- Применить триггер к таблицам
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON public.users 
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_projects_updated_at BEFORE UPDATE ON public.projects 
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_skills_updated_at BEFORE UPDATE ON public.skills 
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column(); 