-- Очистка всех данных проектов с учетом foreign key constraints
-- Сначала очищаем зависимые таблицы, потом основную

-- 1. Очищаем таблицу связей проект-технологии
DELETE FROM project_technologies;

-- 2. Очищаем основную таблицу проектов  
DELETE FROM projects;

-- 3. Сбрасываем последовательности ID
ALTER SEQUENCE projects_id_seq RESTART WITH 1;

-- 4. Проверяем результат
SELECT 
    (SELECT COUNT(*) FROM projects) as projects_count,
    (SELECT COUNT(*) FROM project_technologies) as project_technologies_count;

-- Готово для перезагрузки данных через DataLoader
SELECT 'Все таблицы проектов очищены. Перезапустите backend для загрузки новых данных с WebP путями.' as message; 