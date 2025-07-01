-- Примеры данных для портфолио инженера-программиста
-- Выполните после создания схемы

-- Подключение к базе данных
\c portfolio_db;

-- Создание пользователя администратора (пароль: admin123)
INSERT INTO public.users (username, email, password, role) VALUES 
('admin', 'admin@portfolio.dev', '$2a$10$Xl0yhvzLIxJCDdKnE4.5FeN/d3nYhZGjY3EYjGjOo4A6p0vZjlBKK', 'ADMIN');

-- Добавление навыков
INSERT INTO public.skills (name, description, level, category, years_of_experience, display_order, color) VALUES
-- Микроконтроллеры
('ESP32', 'Разработка IoT устройств на ESP32, работа с Wi-Fi, Bluetooth, различными датчиками и исполнительными механизмами', 95, 'MICROCONTROLLERS', 5, 1, '#00D4AA'),
('STM32', 'Программирование микроконтроллеров STM32, работа с HAL библиотеками, настройка периферии', 90, 'MICROCONTROLLERS', 4, 2, '#03234B'),
('Arduino', 'Быстрое прототипирование на платформе Arduino, создание образовательных проектов', 85, 'MICROCONTROLLERS', 6, 3, '#00979D'),

-- ПЛК
('Siemens STEP 7', 'Программирование ПЛК Siemens S7-300/400/1200/1500 в среде TIA Portal', 88, 'PLC', 3, 4, '#009999'),
('Schneider Unity Pro', 'Разработка программ для ПЛК Schneider Electric Modicon', 82, 'PLC', 2, 5, '#3DCD58'),
('OMRON CX-Programmer', 'Программирование ПЛК OMRON серии CP, CJ, CS', 78, 'PLC', 2, 6, '#1E5FBF'),

-- Языки программирования
('C/C++', 'Низкоуровневое программирование для микроконтроллеров и встроенных систем', 92, 'PROGRAMMING', 5, 7, '#00599C'),
('Python', 'Автоматизация, обработка данных, создание серверных приложений для IoT', 85, 'PROGRAMMING', 4, 8, '#3776AB'),
('Ladder Logic', 'Программирование ПЛК на языке релейных схем', 90, 'PROGRAMMING', 3, 9, '#FF6B35'),

-- Протоколы связи
('Modbus RTU/TCP', 'Промышленный протокол связи для SCADA систем и ПЛК', 88, 'PROTOCOLS', 3, 10, '#FF9500'),
('MQTT', 'Легковесный протокол для IoT устройств и телеметрии', 90, 'PROTOCOLS', 4, 11, '#660066'),
('CAN Bus', 'Автомобильная и промышленная шина данных', 80, 'PROTOCOLS', 2, 12, '#CC0000'),
('Ethernet/IP', 'Промышленный Ethernet для автоматизации', 75, 'PROTOCOLS', 2, 13, '#0066CC'),

-- Инструменты
('KiCad', 'Проектирование печатных плат для встроенных систем', 85, 'TOOLS', 3, 14, '#314CB6'),
('PlatformIO', 'Среда разработки для встроенных систем', 90, 'TOOLS', 4, 15, '#FF7F00'),
('Git', 'Система контроля версий для управления кодом', 88, 'TOOLS', 5, 16, '#F05032'),

-- Аппаратная часть
('Analog Circuits', 'Проектирование аналоговых схем, фильтров, усилителей', 80, 'HARDWARE', 4, 17, '#8E44AD'),
('Digital Logic', 'Цифровая логика, работа с логическими элементами', 85, 'HARDWARE', 5, 18, '#2ECC71'),
('PCB Design', 'Разработка печатных плат, трассировка, размещение компонентов', 82, 'HARDWARE', 3, 19, '#E74C3C');

-- Добавление проектов
INSERT INTO public.projects (title, description, short_description, category, status, featured, display_order, github_url, live_url) VALUES

-- ESP32 проекты
('Умная теплица на ESP32', 
'Система автоматического управления микроклиматом теплицы с использованием ESP32. Включает мониторинг температуры, влажности воздуха и почвы, автоматический полив, управление освещением и вентиляцией. Данные передаются в облако через Wi-Fi, есть веб-интерфейс для удаленного мониторинга и управления.',
'Автоматизированная система управления микроклиматом теплицы с IoT функциональностью',
'ESP32', 'COMPLETED', true, 1, 
'https://github.com/portfolio/smart-greenhouse', 
'http://greenhouse.portfolio.dev'),

('Беспроводная сенсорная сеть', 
'Mesh-сеть из ESP32 устройств для мониторинга окружающей среды на большой территории. Каждый узел измеряет температуру, влажность, давление, качество воздуха и передает данные через ESP-MESH протокол. Центральный узел собирает данные и отправляет в InfluxDB для дальнейшей обработки и визуализации в Grafana.',
'Распределенная сеть датчиков на базе ESP-MESH для экологического мониторинга',
'ESP32', 'COMPLETED', true, 2,
'https://github.com/portfolio/mesh-sensor-network', 
null),

('IoT метеостанция', 
'Персональная метеостанция на ESP32 с набором датчиков для измерения погодных условий. Отображает данные на OLED дисплее, сохраняет историю в локальную память и передает в облачные сервисы. Поддерживает OTA обновления прошивки и имеет веб-интерфейс для настройки.',
'Домашняя метеостанция с облачной синхронизацией и веб-интерфейсом',
'ESP32', 'COMPLETED', false, 3,
'https://github.com/portfolio/weather-station',
'http://weather.portfolio.dev'),

-- STM32 проекты  
('Система безопасности на STM32', 
'Комплексная система безопасности на базе STM32F407 с множественными датчиками движения, магнитными датчиками на окнах и дверях, камерой, GSM модулем для уведомлений. Включает клавиатуру для управления, LCD дисплей, звуковую сигнализацию. Поддерживает несколько зон безопасности и различные режимы работы.',
'Многозонная система безопасности с GSM уведомлениями и видеонаблюдением',
'STM32', 'COMPLETED', true, 4,
'https://github.com/portfolio/security-system',
null),

('CAN Bus автомобильный сканер', 
'Диагностический сканер для автомобилей на STM32 с CAN интерфейсом. Читает диагностические коды ошибок (DTC), отображает параметры двигателя в реальном времени, сохраняет данные на SD карту. Имеет цветной TFT дисплей и простой пользовательский интерфейс.',
'OBD-II сканер с CAN интерфейсом для диагностики автомобилей',
'STM32', 'IN_PROGRESS', false, 5,
'https://github.com/portfolio/can-scanner',
null),

-- ПЛК проекты
('Автоматизация конвейерной линии', 
'Полная автоматизация конвейерной линии упаковки на базе ПЛК Siemens S7-1200. Система включает управление двигателями конвейеров, позиционирование упаковочного оборудования, контроль качества продукции через машинное зрение, автоматическое взвешивание и маркировку. HMI панель для операторского интерфейса.',
'Комплексная автоматизация производственной линии с машинным зрением',
'PLC', 'COMPLETED', true, 6,
null,
null),

('SCADA система водоснабжения', 
'Система диспетчерского управления водонасосной станцией. ПЛК управляет насосами, контролирует уровни в резервуарах, давление в системе, качество воды. SCADA интерфейс с мнемосхемами, трендами, аварийной сигнализацией. Удаленный доступ через VPN для обслуживающего персонала.',
'Диспетчерское управление водонасосной станцией с SCADA интерфейсом',
'PLC', 'COMPLETED', false, 7,
null,
null),

-- IoT проекты
('Умный дом на базе MQTT', 
'Комплексная система умного дома с централизованным управлением через MQTT брокер. Включает управление освещением, климат-контроль, безопасность, мониторинг энергопотребления. Мобильное приложение для удаленного управления, голосовое управление через Алису/Google Assistant.',
'Интегрированная система умного дома с голосовым управлением',
'IOT', 'IN_PROGRESS', true, 8,
'https://github.com/portfolio/smart-home',
'http://home.portfolio.dev'),

('Промышленный IoT мониторинг', 
'Система мониторинга промышленного оборудования в реальном времени. Сбор данных с датчиков вибрации, температуры, давления через промышленные контроллеры. Передача данных в облако для анализа и предиктивного обслуживания. Дашборды в Grafana с алертами и уведомлениями.',
'Предиктивное обслуживание промышленного оборудования через IoT',
'IOT', 'COMPLETED', false, 9,
null,
null);

-- Добавление технологий к проектам
INSERT INTO public.project_technologies (project_id, technology) VALUES
-- Умная теплица
(1, 'ESP32'), (1, 'DHT22'), (1, 'Soil Moisture Sensor'), (1, 'Relay Module'), 
(1, 'Wi-Fi'), (1, 'MQTT'), (1, 'InfluxDB'), (1, 'Grafana'), (1, 'React'),

-- Беспроводная сенсорная сеть  
(2, 'ESP32'), (2, 'ESP-MESH'), (2, 'BME280'), (2, 'MQ-135'), 
(2, 'FreeRTOS'), (2, 'InfluxDB'), (2, 'Grafana'),

-- IoT метеостанция
(3, 'ESP32'), (3, 'BME280'), (3, 'BH1750'), (3, 'OLED SSD1306'),
(3, 'Wi-Fi'), (3, 'ThingSpeak'), (3, 'OTA Updates'),

-- Система безопасности
(4, 'STM32F407'), (4, 'PIR Sensor'), (4, 'GSM Module'), (4, 'Camera Module'),
(4, 'LCD Display'), (4, 'Keypad'), (4, 'FreeRTOS'),

-- CAN сканер
(5, 'STM32F103'), (5, 'CAN Bus'), (5, 'TFT Display'), (5, 'SD Card'),
(5, 'OBD-II'), (5, 'DTC Codes'),

-- Конвейерная линия
(6, 'Siemens S7-1200'), (6, 'TIA Portal'), (6, 'HMI Panel'), (6, 'Servo Motors'),
(6, 'Vision System'), (6, 'Profinet'), (6, 'Ladder Logic'),

-- SCADA водоснабжение
(7, 'Siemens S7-300'), (7, 'WinCC'), (7, 'Profibus'), (7, 'VFD'),
(7, 'Level Sensors'), (7, 'Pressure Sensors'), (7, 'VPN'),

-- Умный дом
(8, 'ESP32'), (8, 'MQTT'), (8, 'Node-RED'), (8, 'Home Assistant'),
(8, 'React Native'), (8, 'Voice Control'), (8, 'Zigbee'),

-- Промышленный IoT
(9, 'ESP32'), (9, 'Modbus RTU'), (9, 'InfluxDB'), (9, 'Grafana'),
(9, 'Vibration Sensors'), (9, 'Machine Learning'), (9, 'Predictive Analytics');

-- Добавление изображений к проектам (пути к placeholder изображениям)
INSERT INTO public.project_images (project_id, image_path) VALUES
(1, '/images/projects/greenhouse-1.jpg'),
(1, '/images/projects/greenhouse-2.jpg'),
(1, '/images/projects/greenhouse-dashboard.jpg'),

(2, '/images/projects/mesh-network-1.jpg'),
(2, '/images/projects/mesh-network-topology.jpg'),

(3, '/images/projects/weather-station-1.jpg'),
(3, '/images/projects/weather-station-web.jpg'),

(4, '/images/projects/security-system-1.jpg'),
(4, '/images/projects/security-hmi.jpg'),

(5, '/images/projects/can-scanner-1.jpg'),
(5, '/images/projects/can-scanner-display.jpg'),

(6, '/images/projects/conveyor-line-1.jpg'),
(6, '/images/projects/conveyor-hmi.jpg'),

(7, '/images/projects/scada-main.jpg'),
(7, '/images/projects/water-pumps.jpg'),

(8, '/images/projects/smart-home-app.jpg'),
(8, '/images/projects/smart-home-devices.jpg'),

(9, '/images/projects/industrial-iot-dashboard.jpg'),
(9, '/images/projects/industrial-sensors.jpg');

-- Вывод информации о добавленных данных
SELECT 'Добавлено пользователей:' as info, COUNT(*) as count FROM public.users
UNION ALL
SELECT 'Добавлено навыков:', COUNT(*) FROM public.skills  
UNION ALL
SELECT 'Добавлено проектов:', COUNT(*) FROM public.projects
UNION ALL
SELECT 'Добавлено технологий:', COUNT(*) FROM public.project_technologies
UNION ALL  
SELECT 'Добавлено изображений:', COUNT(*) FROM public.project_images;

-- Успешное завершение
\echo 'Примеры данных успешно добавлены!'
\echo 'Логин для админ панели: admin / admin123' 