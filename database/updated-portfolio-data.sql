-- Обновленные реальные данные портфолио Султанова Тимура
-- Основано на резюме от 24 мая 2025

-- Очистка существующих данных (с проверкой существования таблиц)
DELETE FROM work_experience WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'work_experience');
DELETE FROM project_images WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'project_images');
DELETE FROM project_technologies WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'project_technologies');
DELETE FROM projects WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'projects');
DELETE FROM skills WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'skills');

-- Создание таблиц если они не существуют
CREATE TABLE IF NOT EXISTS skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    level INTEGER DEFAULT 0,
    category VARCHAR(100),
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    is_featured BOOLEAN DEFAULT FALSE,
    display_order INTEGER DEFAULT 0,
    github_url VARCHAR(500),
    demo_url VARCHAR(500),
    technologies TEXT, -- JSON array as text
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS project_images (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    image_url VARCHAR(500) NOT NULL,
    alt_text VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS project_technologies (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    technology_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS work_experience (
    id BIGSERIAL PRIMARY KEY,
    position VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    start_date DATE NOT NULL,
    end_date DATE,
    is_current BOOLEAN DEFAULT FALSE,
    description TEXT,
    technologies TEXT,
    achievements TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Добавление реальных навыков из резюме
INSERT INTO skills (name, description, level, category, display_order) VALUES
-- Микроконтроллеры
('STM32', 'Программирование ARM микроконтроллеров STM32 различных серий', 95, 'МИКРОКОНТРОЛЛЕРЫ', 1),
('ESP32', 'Разработка IoT устройств на ESP32 с ESP-IDF', 90, 'МИКРОКОНТРОЛЛЕРЫ', 2),
('Texas Instruments Tiva', 'Программирование микроконтроллеров Tiva (Texas Instrument)', 85, 'МИКРОКОНТРОЛЛЕРЫ', 3),
('RISC-V', 'Программирование RISC-V микроконтроллеров', 80, 'МИКРОКОНТРОЛЛЕРЫ', 4),
('Atmel AVR', 'Программирование микроконтроллеров Atmel (ATtiny, ATmega)', 80, 'МИКРОКОНТРОЛЛЕРЫ', 5),

-- ПЛК и промышленная автоматизация
('Omron PLC', 'Программирование промышленных контроллеров Omron (SysMac Studio)', 90, 'ПЛК', 6),
('JUMO mTRON T', 'Программирование контроллеров JUMO для промышленных процессов', 85, 'ПЛК', 7),
('Danfoss PLC', 'Программирование контроллеров Danfoss', 80, 'ПЛК', 8),
('Honeywell BMS', 'Системы диспетчеризации зданий Honeywell', 85, 'ПЛК', 9),
('CodeSys', 'Программирование ПЛК в среде CodeSys', 85, 'ПЛК', 10),
('SCADA системы', 'Разработка SCADA систем для промышленной автоматизации', 90, 'ПЛК', 11),

-- Проектирование и схемотехника
('Altium Designer', 'Проектирование печатных плат в Altium Designer', 90, 'ПРОЕКТИРОВАНИЕ', 12),
('Схемотехника', 'Разработка электронных схем любой сложности', 95, 'ПРОЕКТИРОВАНИЕ', 13),
('Силовая электроника', 'Разработка устройств силовой электроники', 85, 'ПРОЕКТИРОВАНИЕ', 14),

-- Протоколы связи
('Modbus', 'Реализация протокола Modbus RTU/TCP', 85, 'ПРОТОКОЛЫ', 15),
('WiFi', 'Реализация WiFi соединений и IoT устройств', 85, 'ПРОТОКОЛЫ', 16),
('GSM/SMS', 'Разработка GSM модулей с поддержкой SMS (SIM800)', 80, 'ПРОТОКОЛЫ', 17),
('4-20mA', 'Работа с промышленными датчиками 4-20mA', 85, 'ПРОТОКОЛЫ', 18),

-- Программирование
('C/C++', 'Программирование микроконтроллеров на C/C++', 95, 'ПРОГРАММИРОВАНИЕ', 19),
('Java Spring Boot', 'Backend разработка на Java Spring Boot', 85, 'ПРОГРАММИРОВАНИЕ', 20),
('PostgreSQL', 'Проектирование баз данных PostgreSQL', 80, 'ПРОГРАММИРОВАНИЕ', 21),
('JPA/Hibernate', 'Работа с JPA и Hibernate', 75, 'ПРОГРАММИРОВАНИЕ', 22),
('REST API', 'Разработка REST API для документооборота', 80, 'ПРОГРАММИРОВАНИЕ', 23),
('Dart/Flutter', 'Мобильная разработка на Flutter', 70, 'ПРОГРАММИРОВАНИЕ', 24),
('Kotlin', 'Программирование на Kotlin', 65, 'ПРОГРАММИРОВАНИЕ', 25);

-- Добавление реальных проектов из опыта работы
INSERT INTO projects (title, description, category, status, is_featured, display_order, github_url, demo_url, technologies) VALUES

('Система контроля утечки газа (автомобильная и бытовая)',
'Проектировал плату контроля утечки газа (метан, пропан, угарный газ) для автомобилей и бытового применения. Автомобильная версия включает три зоны контроля, а домашняя версия оснащена функцией автоматического переключения на аккумулятор при отключении питания, а также возможностью оповещения через GSM-модуль SIM800 и Wi-Fi-модуль ESP32.',
'АВТОМАТИЗАЦИЯ', 'COMPLETED', true, 1,
'https://github.com/timurtm72?tab=repositories',
NULL,
'["STM32", "ESP32", "GSM SIM800", "WiFi", "Датчики газа", "C/C++"]'),

('REST API для документооборота',
'Разработал проект REST API для документооборота с использованием Spring Boot, PostgreSQL, JPA, Hibernate. Включает валидацию данных, работу с JSON, интеграцию со сторонним API, использование DTO, Spring Security. Работа по Scrum (спринты по 2 недели). Отвечал за проектирование базы данных.',
'WEB_РАЗРАБОТКА', 'COMPLETED', true, 2,
'https://github.com/timurtm72?tab=repositories',
NULL,
'["Java", "Spring Boot", "PostgreSQL", "JPA", "Hibernate", "REST API", "Spring Security"]'),

('Система управления станцией перегонки нефти',
'Проектировал и разрабатывал программы для ПЛК компании JUMO (Fulda) для автоматизации системы управления станцией перегонки нефти. Полная автоматизация технологического процесса.',
'ПЛК', 'COMPLETED', true, 3,
NULL, NULL,
'["JUMO mTRON T", "ПЛК", "Автоматизация", "Нефтепереработка"]'),

('Система управления станком отделения семян хлопка',
'Проектировал систему управления станком по отделению семян хлопка от сырца на базе микроконтроллера STM32F407 и TFT SSD1963. Все платы управления и контроля спроектировал сам и изготовил, тестирование происходило в течении 2 лет, станок работает.',
'АВТОМАТИЗАЦИЯ', 'COMPLETED', true, 4,
NULL, NULL,
'["STM32F407", "TFT SSD1963", "C/C++", "Промышленная автоматика"]'),

('Автоматизация парников (10 гектаров)',
'Разрабатывал и программировал щиты управления насосами, а также настраивал SCADA-систему израильского производства для автоматизации парников площадью 10 гектаров.',
'АВТОМАТИЗАЦИЯ', 'COMPLETED', false, 5,
NULL, NULL,
'["SCADA", "Управление насосами", "Автоматизация теплиц", "ПЛК"]'),

('Щиты управления КНС на базе ПЛК Danfoss',
'Собирал и запускал щиты управления КНС (канализационно-насосных станций) на базе ПЛК Danfoss. Полная автоматизация работы насосной станции.',
'АВТОМАТИЗАЦИЯ', 'COMPLETED', false, 6,
NULL, NULL,
'["Danfoss PLC", "КНС", "Автоматизация", "Управление насосами"]'),

('Контроллеры управления освещением',
'Разработка контроллеров управления освещением. Создание системы удалённого мониторинга освещения. Работа с C/C++, микроконтроллерами и драйверами.',
'IoT', 'COMPLETED', false, 7,
NULL, NULL,
'["C/C++", "Микроконтроллеры", "LED драйверы", "Удаленный мониторинг"]'),

('Системы BMS управления зданиями',
'Обслуживание и доработка систем автоматизации и диспетчеризации (BMS) управления зданиями в области электрооборудования и климат-контроля на базе ПЛК Honeywell и SCADA-систем.',
'ПЛК', 'COMPLETED', true, 8,
NULL, NULL,
'["Honeywell PLC", "BMS", "SCADA", "Климат-контроль", "Диспетчеризация"]'),

('Программы для заполнения карточек пациентов',
'Писал программы на языке Turbo Pascal для заполнения карточек пациентов и перенос данных в базу данных в НИИ охраны здоровья матери и ребенка.',
'МЕДИЦИНА', 'COMPLETED', false, 9,
NULL, NULL,
'["Turbo Pascal", "База данных", "Медицинская информатика"]'),

('Промышленные контроллеры (SysMac Studio)',
'Программирование промышленных контроллеров в SysMac Studio, CodeSys, SCADA, HMI. Разработка и отладка алгоритмов управления оборудованием.',
'ПЛК', 'COMPLETED', true, 10,
NULL, NULL,
'["SysMac Studio", "CodeSys", "SCADA", "HMI", "Omron PLC"]'),

('Встраиваемые системы ESP32/STM32/RISC-V',
'Написание ПО для встраиваемых систем на ESP32, STM32, RISC-V. Разработка прошивок для различных промышленных применений.',
'МИКРОКОНТРОЛЛЕРЫ', 'COMPLETED', true, 11,
'https://github.com/timurtm72?tab=repositories',
NULL,
'["ESP32", "STM32", "RISC-V", "C/C++", "Встраиваемые системы"]');

-- Добавление реального опыта работы из резюме (25 лет опыта)
INSERT INTO work_experience (
    position, 
    company, 
    location, 
    start_date, 
    end_date, 
    is_current, 
    description, 
    technologies, 
    achievements
) VALUES
(
    'Инженер-программист',
    'ООО «Проект 24»',
    'Иннополис',
    '2022-02-01',
    NULL,
    TRUE,
    'Программирование промышленных контроллеров (SysMac Studio, CodeSys, SCADA, HMI). Разработка и отладка алгоритмов управления оборудованием. Написание ПО для встраиваемых систем на ESP32, STM32, RISC-V.',
    'SysMac Studio, CodeSys, SCADA, HMI, ESP32, STM32, RISC-V, C/C++',
    'Успешная реализация проектов автоматизации, разработка надежных алгоритмов управления'
),
(
    'Java-программист',
    'Maxima OOO',
    'Казань',
    '2022-11-01',
    '2023-06-30',
    FALSE,
    'Разработал проект REST API для документооборота: Spring Boot, PostgreSQL, JPA, Hibernate. Валидация данных, работа с JSON. Интеграция со сторонним API. Использование DTO, Spring Security. Работа по Scrum (спринты по 2 недели). Отвечал за проектирование базы данных.',
    'Java, Spring Boot, PostgreSQL, JPA, Hibernate, REST API, Spring Security, JSON, DTO, Scrum',
    'Успешно реализован полнофункциональный REST API для документооборота с интеграцией внешних сервисов'
),
(
    'Инженер-программист',
    'ООО "Челны свет"',
    'Набережные Челны',
    '2021-03-01',
    '2021-12-31',
    FALSE,
    'Разработка контроллеров управления освещением. Создание системы удалённого мониторинга освещения. Работа с C/C++, микроконтроллерами и драйверами.',
    'C/C++, микроконтроллеры, LED драйверы, удаленный мониторинг',
    'Создана эффективная система управления и мониторинга освещения'
),
(
    'Инженер программист микроконтроллеров',
    'ООО "Водий нефтегазовик энергия"',
    'Ташкент',
    '2017-08-01',
    '2018-04-30',
    FALSE,
    'Проектировал плату контроля утечки газа (метан, пропан, угарный газ) для автомобилей и бытового применения на языке С/С++. В управляющей плате использован микроконтроллер STM32. Автомобильная версия включает три зоны контроля, а домашняя версия оснащена функцией автоматического переключения на аккумулятор при отключении питания, а также возможностью оповещения через GSM-модуль SIM800 и Wi-Fi-модуль ESP32,RISC-V.',
    'STM32, ESP32, RISC-V, GSM SIM800, Wi-Fi, C/C++, датчики газа',
    'Разработана надежная система контроля утечки газа для автомобильного и бытового применения'
),
(
    'Инженер-программист АСУ ТП',
    'ХО "Восполнение"',
    'Туркменистан',
    '2015-04-01',
    '2017-07-31',
    FALSE,
    'Обслуживание и доработка систем автоматизации и диспетчеризации (BMS) управления зданиями в области электрооборудования и климат-контроля на базе ПЛК Honeywell и SCADA-систем. В случае выхода из строя плат управления щитами разрабатывал новые платы и писал прошивки на базе микроконтроллеров STM32, Texas Instruments и Atmel на языке С/С++,RISC-V.',
    'Honeywell PLC, SCADA, BMS, STM32, Texas Instruments, Atmel, RISC-V, C/C++',
    'Обеспечена стабильная работа систем автоматизации зданий, разработаны замещающие платы управления'
),
(
    'Инженер-программист АСУ ТП',
    'Филиал компании «Групарт пумпен Фертриеб ГМБХ»',
    'Туркменистан',
    '2013-07-01',
    '2015-02-28',
    FALSE,
    'Проектировал и разрабатывал программы для ПЛК компании JUMO (Fulda) для автоматизации системы управления станцией перегонки нефти. Собирал и запускал щиты управления КНС на базе ПЛК Danfoss. Разрабатывал и программировал щиты управления насосами, а также настраивал SCADA-систему израильского производства для автоматизации парников площадью 10 гектаров.',
    'JUMO mTRON T, Danfoss PLC, SCADA, автоматизация, управление насосами',
    'Успешно автоматизирована станция перегонки нефти, запущена система автоматизации парников на 10 гектаров'
),
(
    'Инженер-программист АСУ ТП',
    'Банк Внешнеэкономической деятельности Туркменистана',
    'Туркменистан',
    '2005-08-01',
    '2013-05-31',
    FALSE,
    'Выполнял контроль и управление системами диспетчеризации здания банка BMS. Обслуживание и доработка систем автоматизации и диспетчеризации (BMS) управления зданиями в области электрооборудования и климат-контроля на базе ПЛК Honeywell и SCADA-систем. В случае выхода из строя плат управления щитами разрабатывал новые платы и писал прошивки на базе микроконтроллеров STM32, Texas Instruments и Atmel на языке С/С++,RISC-V.',
    'Honeywell PLC, BMS, SCADA, STM32, Texas Instruments, Atmel, RISC-V, C/C++',
    'Обеспечена надежная работа систем диспетчеризации здания банка на протяжении 8 лет'
),
(
    'Инженер-программист',
    'Министерство хлопка Туркменистана',
    'Туркменистан',
    '2000-02-01',
    '2005-06-30',
    FALSE,
    'Проектировал систему управления станком по отделению семян хлопка от сырца на базе мк STM32F407 и TFT SSD1963 на языке С/С++. Все платы управления и контроля спроектировал сам и изготовил, тестирование происходило в течении 2 лет, станок работает.',
    'STM32F407, TFT SSD1963, C/C++, проектирование плат',
    'Создана и внедрена автоматизированная система управления станком, успешно работает более 20 лет'
),
(
    'Инженер-программист',
    'Научно исследовательский институт охраны здоровья матери и ребенка',
    'Туркменистан',
    '1997-05-01',
    '2000-01-31',
    FALSE,
    'Писал программы на языке Turbo Pascal для заполнения карточек пациентов и перенос данных в базу данных.',
    'Turbo Pascal, базы данных, медицинская информатика',
    'Автоматизирован процесс ведения медицинской документации пациентов'
);

-- Добавление изображений проектов (только если таблица существует)
INSERT INTO project_images (project_id, image_url, alt_text, is_primary) 
SELECT 1, '/images/projects/gas-detector.jpg', 'Система контроля утечки газа', true
WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'project_images');

INSERT INTO project_images (project_id, image_url, alt_text, is_primary) 
SELECT 2, '/images/projects/rest-api.jpg', 'REST API для документооборота', true
WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'project_images');

INSERT INTO project_images (project_id, image_url, alt_text, is_primary) 
SELECT 3, '/images/projects/oil-refinery.jpg', 'Система управления станцией перегонки нефти', true
WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'project_images');

INSERT INTO project_images (project_id, image_url, alt_text, is_primary) 
SELECT 4, '/images/projects/cotton-machine.jpg', 'Система управления станком отделения семян хлопка', true
WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'project_images');

INSERT INTO project_images (project_id, image_url, alt_text, is_primary) 
SELECT 5, '/images/projects/greenhouse.jpg', 'Автоматизация парников', true
WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'project_images'); 