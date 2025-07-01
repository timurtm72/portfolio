-- ===============================================
-- ПРИНУДИТЕЛЬНАЯ ОЧИСТКА ТАБЛИЦЫ ПРОЕКТОВ
-- Для удаления лишних проектов и оставления только реальных
-- ===============================================

-- Проверим сколько проектов сейчас в БД
SELECT 'Проектов до очистки:' as info, COUNT(*) as count FROM projects;

-- Очищаем связанные таблицы сначала
DELETE FROM project_technologies;
DELETE FROM project_images;

-- Очищаем основную таблицу проектов
DELETE FROM projects;

-- Сбрасываем последовательность для ID
ALTER SEQUENCE projects_id_seq RESTART WITH 1;

-- Проверим результат
SELECT 'Проектов после очистки:' as info, COUNT(*) as count FROM projects;

-- Готово для загрузки новых данных
SELECT 'Таблица projects очищена. Перезапустите backend для загрузки только реальных проектов.' as message;

-- Очистка таблицы проектов для перезагрузки с новыми путями к изображениям
DELETE FROM projects;

-- Сброс последовательности ID (если нужно)
ALTER SEQUENCE projects_id_seq RESTART WITH 1;

-- Проверка результата
SELECT COUNT(*) as project_count FROM projects; 