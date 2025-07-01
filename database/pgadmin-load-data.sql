-- ========================================
-- SQL СКРИПТ ДЛЯ PGADMIN - ПОРТФОЛИО ТИМУРА СУЛТАНОВА
-- Резюме обновлено: 24 мая 2025 в 20:55
-- ========================================

-- 1. СОЗДАНИЕ ТАБЛИЦ
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    bio TEXT,
    location VARCHAR(255),
    phone VARCHAR(20),
    github_url VARCHAR(255),
    portfolio_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS skills (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    proficiency_level INTEGER CHECK (proficiency_level >= 1 AND proficiency_level <= 5),
    years_of_experience INTEGER,
    description TEXT,
    is_featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS projects (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    technologies TEXT,
    start_date DATE,
    end_date DATE,
    github_url VARCHAR(255),
    is_featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS work_experience (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    position VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    start_date DATE NOT NULL,
    end_date DATE,
    is_current BOOLEAN DEFAULT FALSE,
    description TEXT,
    technologies TEXT,
    achievements TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS education (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    institution VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    field VARCHAR(255),
    year INTEGER,
    description TEXT,
    is_completed BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. ОЧИСТКА ДАННЫХ
TRUNCATE TABLE users RESTART IDENTITY CASCADE;
TRUNCATE TABLE skills RESTART IDENTITY CASCADE;
TRUNCATE TABLE projects RESTART IDENTITY CASCADE;
TRUNCATE TABLE work_experience RESTART IDENTITY CASCADE;
TRUNCATE TABLE education RESTART IDENTITY CASCADE;

-- 3. ДАННЫЕ ПОЛЬЗОВАТЕЛЯ
INSERT INTO users (username, email, first_name, last_name, bio, location, phone, github_url, portfolio_url, password) VALUES (
    'timur_sultanov',
    'timur.sultanov1972@gmail.com',
    'Тимур',
    'Султанов',
    'Инженер-программист с 25-летним опытом работы в области микроконтроллеров, ПЛК и АСУ ТП. Специализируюсь на STM32, ESP32, Texas Instruments, RISC-V. Разрабатываю промышленные системы автоматизации, системы управления оборудованием и встраиваемые решения.',
    'Иннополис, Россия',
    '+7 (927) 244-40-51',
    'https://github.com/timurtm72',
    'https://technocom.site123.me',
    '$2a$10$example.password.hash'
);

-- 4. НАВЫКИ И ТЕХНОЛОГИИ
INSERT INTO skills (name, category, proficiency_level, years_of_experience, description, is_featured) VALUES
('STM32', 'Микроконтроллеры', 5, 15, 'STM32F407, STM32L152, разработка встраиваемых систем', true),
('ESP32', 'Микроконтроллеры', 5, 8, 'Wi-Fi и Bluetooth модули, IoT системы', true),
('Texas Instruments', 'Микроконтроллеры', 4, 10, 'Tiva, системы управления промышленным оборудованием', false),
('RISC-V', 'Микроконтроллеры', 3, 3, 'Современная архитектура процессоров', false),
('Omron PLC', 'ПЛК', 5, 8, 'SysMac Studio, программирование промышленных контроллеров', true),
('JUMO mTRON T', 'ПЛК', 5, 5, 'Системы автоматизации нефтеперегонных станций', false),
('Honeywell PLC', 'ПЛК', 4, 10, 'BMS системы управления зданиями', false),
('CodeSys', 'SCADA', 5, 8, 'Среда разработки для ПЛК', true),
('SCADA системы', 'SCADA', 4, 12, 'Диспетчерские системы управления', false),
('C/C++', 'Программирование', 5, 20, 'Основной язык для микроконтроллеров', true),
('Java', 'Программирование', 4, 4, 'Spring Boot, backend разработка', true),
('Spring Boot', 'Backend', 4, 2, 'REST API, микросервисы', true),
('PostgreSQL', 'Базы данных', 4, 3, 'Реляционные БД, JPA, Hibernate', true),
('Modbus', 'Протоколы', 4, 8, 'Промышленные протоколы связи', false),
('GSM SIM800', 'Протоколы', 4, 5, 'Модули мобильной связи', false),
('Wi-Fi ESP32', 'Протоколы', 4, 6, 'Беспроводные сети', false),
('Автоматизация производства', 'Автоматизация', 5, 20, '25 лет опыта в промышленной автоматизации', true),
('АСУ ТП', 'Автоматизация', 5, 18, 'Автоматизированные системы управления', true),
('BMS системы', 'Автоматизация', 4, 12, 'Системы управления зданиями', false);

-- 5. ОПЫТ РАБОТЫ
INSERT INTO work_experience (position, company, location, start_date, end_date, is_current, description, technologies, achievements) VALUES
(
    'Инженер-программист',
    'ООО «Проект 24»',
    'Иннополис',
    '2022-02-01',
    NULL,
    TRUE,
    'Программирование промышленных контроллеров (SysMac Studio, CodeSys, SCADA, HMI). Разработка и отладка алгоритмов управления оборудованием. Написание ПО для встраиваемых систем на ESP32, STM32, RISC-V.',
    'SysMac Studio, CodeSys, SCADA, HMI, ESP32, STM32, RISC-V, C/C++',
    'Автоматизация промышленных процессов; Разработка алгоритмов управления; Создание ПО для встраиваемых систем'
),
(
    'Java-программист',
    'Maxima OOO',
    'Казань',
    '2022-11-01',
    '2023-06-30',
    FALSE,
    'Разработал проект REST API для документооборота: Spring Boot, PostgreSQL, JPA, Hibernate. Валидация данных, работа с JSON. Интеграция со сторонним API. Использование DTO, Spring Security. Работа по Scrum.',
    'Java, Spring Boot, PostgreSQL, JPA, Hibernate, REST API, JSON, Spring Security',
    'Разработан REST API для документооборота; Спроектирована база данных; GitHub: https://github.com/timurtm72'
),
(
    'Инженер-программист',
    'ООО "Челны свет"',
    'Набережные Челны',
    '2021-03-01',
    '2021-12-31',
    FALSE,
    'Разработка контроллеров управления освещением. Создание системы удалённого мониторинга освещения. Работа с C/C++, микроконтроллерами и драйверами.',
    'C/C++, микроконтроллеры, драйверы, системы освещения',
    'Разработаны контроллеры управления освещением; Создана система удалённого мониторинга'
),
(
    'Инженер программист микроконтроллеров',
    'ООО "Водий нефтегазовик энергия"',
    'Ташкент',
    '2017-08-01',
    '2018-04-30',
    FALSE,
    'Проектировал плату контроля утечки газа (метан, пропан, угарный газ) для автомобилей и бытового применения на языке С/С++. Автомобильная версия включает три зоны контроля, домашняя - с GSM и Wi-Fi оповещением.',
    'STM32, C/C++, GSM SIM800, ESP32, RISC-V, Wi-Fi, газовые датчики',
    'Разработана плата контроля утечки газа; Автомобильная и домашняя версии; Интеграция GSM и Wi-Fi'
),
(
    'Инженер-программист АСУ ТП',
    'ХО "Восполнение"',
    'Туркменистан',
    '2015-04-01',
    '2017-07-31',
    FALSE,
    'Обслуживание и доработка систем автоматизации и диспетчеризации (BMS) управления зданиями. При выходе из строя плат управления разрабатывал новые платы и писал прошивки на базе STM32, Texas Instruments и Atmel.',
    'BMS, ПЛК Honeywell, SCADA, STM32, Texas Instruments, Atmel, C/C++',
    'Автоматизация и диспетчеризация зданий; Разработка плат управления; Системы климат-контроля'
),
(
    'Инженер-программист АСУ ТП',
    'Филиал компании «Групарт пумпен Фертриеб ГМБХ»',
    'Туркменистан',
    '2013-07-01',
    '2015-02-28',
    FALSE,
    'Проектировал программы для ПЛК JUMO для автоматизации станции перегонки нефти. Собирал щиты управления КНС на базе ПЛК Danfoss. Настраивал SCADA-систему для автоматизации парников 10 гектаров.',
    'ПЛК JUMO, ПЛК Danfoss, SCADA, системы управления насосами',
    'Автоматизация станции перегонки нефти; Щиты управления КНС; Автоматизация парников 10 га'
),
(
    'Инженер-программист АСУ ТП',
    'Банк Внешнеэкономической деятельности Туркменистана',
    'Туркменистан',
    '2005-08-01',
    '2013-05-31',
    FALSE,
    'Контроль и управление системами диспетчеризации здания банка BMS. Обслуживание систем автоматизации на базе ПЛК Honeywell. Разработка новых плат и прошивок при выходе из строя оборудования.',
    'BMS, ПЛК Honeywell, SCADA, STM32, Texas Instruments, Atmel, C/C++',
    'Диспетчеризация здания банка; Системы климат-контроля; 7+ лет в банковской сфере'
),
(
    'Инженер-программист',
    'Министерство хлопка Туркменистана',
    'Туркменистан',
    '2000-02-01',
    '2005-06-30',
    FALSE,
    'Проектировал систему управления станком по отделению семян хлопка от сырца на базе STM32F407 и TFT SSD1963. Все платы спроектировал и изготовил сам, тестирование 2 года.',
    'STM32F407, TFT SSD1963, C/C++, проектирование плат',
    'Система управления станком для хлопка; Полный цикл разработки; Успешный запуск'
),
(
    'Инженер-программист',
    'НИИ охраны здоровья матери и ребенка',
    'Туркменистан',
    '1997-05-01',
    '2000-01-31',
    FALSE,
    'Писал программы на языке Turbo Pascal для заполнения карточек пациентов и перенос данных в базу данных.',
    'Turbo Pascal, базы данных, медицинские информационные системы',
    'Медицинские информационные системы; Автоматизация карточек пациентов'
);

-- 6. ОБРАЗОВАНИЕ И КУРСЫ
INSERT INTO education (type, institution, title, field, year, description, is_completed) VALUES
('degree', 'Туркменский политехнический институт', 'Инженер электрик', 'Техническая кибернетика', 1996, 'Высшее техническое образование', TRUE),
('course', 'Maxima', 'Обучение Java Spring', 'Java, Spring Framework', 2023, 'Backend разработка', TRUE),
('course', 'Udemy', 'Обучение Dart, Flutter', 'Мобильная разработка', 2022, 'Flutter приложения', TRUE),
('certification', 'Omron Electronics, Москва', 'Sysmac. Базовая автоматизация', 'ПЛК Омрон', 2022, 'Сертификация ПЛК', TRUE),
('course', 'Udemy', 'Java - получи черный пояс', 'Java продвинутый', 2021, 'Продвинутая Java', TRUE),
('course', 'Udemy', 'Полный курс Java с нуля', 'Java основы', 2020, 'Основы Java', TRUE),
('course', 'Udemy', 'Android + Java с нуля', 'Android разработка', 2020, 'Мобильные приложения', TRUE),
('course', 'Udemy', 'Котлин - быстрый старт', 'Kotlin', 2020, 'Язык Kotlin', TRUE),
('course', 'Интуит', 'Язык программирования Java', 'Java', 2017, 'Основы Java', TRUE),
('certification', 'Jumo, Fulda, Germany', 'АСУ ТП PLC JUMO mTRON T', 'Промышленная автоматизация', 2014, 'ПЛК JUMO', TRUE),
('course', 'Интуит', 'Основы программирования на языке Си', 'C/C++', 2012, 'Системное программирование', TRUE);

-- 7. ПРОЕКТЫ
INSERT INTO projects (title, description, technologies, start_date, end_date, github_url, is_featured) VALUES
(
    'REST API для документооборота',
    'Полнофункциональный REST API с валидацией данных, интеграцией со сторонними API, Spring Security аутентификацией. Проектирование базы данных PostgreSQL.',
    'Java, Spring Boot, PostgreSQL, JPA, Hibernate, REST API, Spring Security',
    '2022-11-01',
    '2023-06-30',
    'https://github.com/timurtm72',
    TRUE
),
(
    'Система контроля утечки газа',
    'Плата контроля утечки газа для автомобилей и дома. Автомобильная версия с 3 зонами контроля, домашняя с GSM/Wi-Fi оповещением и автоматическим переключением питания.',
    'STM32, C/C++, GSM SIM800, ESP32, RISC-V, газовые датчики',
    '2017-08-01',
    '2018-04-30',
    NULL,
    TRUE
),
(
    'Система управления станком для хлопка',
    'Система управления станком по отделению семян хлопка на базе STM32F407 с TFT дисплеем. Полный цикл: проектирование плат, изготовление, 2 года тестирования.',
    'STM32F407, TFT SSD1963, C/C++, схемотехника',
    '2000-02-01',
    '2005-06-30',
    NULL,
    TRUE
),
(
    'Автоматизация нефтеперегонной станции',
    'Программы для ПЛК JUMO для автоматизации станции перегонки нефти. Щиты управления КНС на ПЛК Danfoss и системы управления насосами.',
    'ПЛК JUMO, ПЛК Danfoss, SCADA, системы управления насосами',
    '2013-07-01',
    '2015-02-28',
    NULL,
    FALSE
),
(
    'Автоматизация тепличного хозяйства',
    'SCADA-система для автоматизации парников площадью 10 гектаров. Контроль климата, полива и освещения.',
    'SCADA системы, климат-контроль, автоматизация',
    '2013-07-01',
    '2015-02-28',
    NULL,
    FALSE
),
(
    'BMS система банка',
    'Системы автоматизации и диспетчеризации здания банка. Электрооборудование и климат-контроль на ПЛК Honeywell с разработкой собственных плат.',
    'BMS, ПЛК Honeywell, SCADA, STM32, климат-контроль',
    '2005-08-01',
    '2013-05-31',
    NULL,
    FALSE
);

-- 8. ПРОВЕРКА ДАННЫХ
SELECT 'Загружено записей:' as info;
SELECT 'users' as table_name, COUNT(*) FROM users
UNION ALL SELECT 'skills', COUNT(*) FROM skills
UNION ALL SELECT 'projects', COUNT(*) FROM projects  
UNION ALL SELECT 'work_experience', COUNT(*) FROM work_experience
UNION ALL SELECT 'education', COUNT(*) FROM education;

-- ГОТОВО! Данные портфолио Тимура Султанова загружены.
-- YouTube: https://youtube.com/channel/UCGnkZF95JhfO7tGaB-oHC6A
-- GitHub: https://github.com/timurtm72
-- Портфолио: https://technocom.site123.me 