# Загрузка реальных данных портфолио

Этот документ описывает, как загрузить реальные данные портфолио Султанова Тимура в базу данных PostgreSQL.

## Способ 1: Через pgAdmin (Рекомендуется)

1. **Откройте pgAdmin**
2. **Подключитесь к серверу PostgreSQL**
3. **Выберите базу данных `portfolio_db`**
4. **Нажмите правой кнопкой мыши на базе данных** → `Query Tool`
5. **Откройте файл `real-portfolio-data.sql`** (можно скопировать содержимое)
6. **Выполните SQL скрипт** (нажмите F5 или кнопку Execute)

## Способ 2: Через командную строку

Если у вас установлен PostgreSQL и настроен PATH:

```bash
# Переход в директорию с базой данных
cd database

# Выполнение SQL скрипта
psql -h localhost -U portfolio_user -d portfolio_db -f real-portfolio-data.sql
```

## Способ 3: Через DBeaver или другой клиент

1. **Подключитесь к базе данных**:
   - Host: `localhost`
   - Port: `5432`
   - Database: `portfolio_db`
   - Username: `portfolio_user`
   - Password: `portfolio_password`

2. **Откройте новый SQL редактор**
3. **Скопируйте содержимое файла `real-portfolio-data.sql`**
4. **Выполните скрипт**

## Что содержит скрипт

### Навыки (25 записей):
- **Микроконтроллеры**: STM32, ESP32, Texas Instruments Tiva, Atmel AVR, Nuvoton
- **ПЛК**: Omron PLC, ОВЕН ПЛК, СПК107, JUMO, SCADA системы, CodeSys
- **Проектирование**: Altium Designer, Easy EDA, Схемотехника, Силовая электроника
- **Протоколы**: Modbus, Bluetooth, WiFi, GSM/SMS, 4-20mA
- **Программирование**: Java Spring Boot, Android Java, C/C++, Python Django, Firebase

### Проекты (16 записей):
1. **USB Bluetooth Mass Storage для швейных машин**
2. **Сигнализатор утечки газа автомобильный**
3. **Система управления инкубатором**
4. **Система управления насосами подачи воды**
5. **Стабилизатор переменного напряжения 20кВт**
6. **LED контроллеры с Modbus**
7. **Сигнализатор утечки газа домашний**
8. **Щит управления 4 насосами**
9. **Система климат-контроля ОВЕН**
10. **Проект станции перегонки нефти**
11. **Индикатор низкого напряжения**
12. **Контроль доступа**
13. **Управление автоматическими дверьми**
14. **GSM модуль с многоязычными SMS**
15. **Блоки питания 5В**
16. **Умный дом Python Django**

### Примеры изображений проектов
Скрипт также добавляет примеры изображений для основных проектов.

## Проверка загрузки данных

После выполнения скрипта можете проверить данные запросами:

```sql
-- Проверка навыков
SELECT name, level, category FROM skills ORDER BY category, display_order;

-- Проверка проектов
SELECT title, category, status, is_featured FROM projects ORDER BY display_order;

-- Проверка изображений
SELECT p.title, pi.image_url, pi.is_primary 
FROM projects p 
LEFT JOIN project_images pi ON p.id = pi.project_id 
WHERE pi.is_primary = true;
```

## Контактная информация

Реальные контактные данные, загруженные в систему:
- **Телефон**: +7 927 244-40-51
- **Email**: timursultanw@yandex.ru, timur.sultanov1972@gmail.com
- **Местоположение**: Иннополис, Татарстан, Россия
- **GitHub**: https://github.com/timurtm72
- **YouTube**: https://youtube.com/channel/UCGnkZF95JhfO7tGaB-oHC6A

## Готово!

После загрузки данных ваше портфолио будет содержать реальную информацию и проекты. 
Перейдите на http://localhost:3000 чтобы увидеть обновленный сайт. 