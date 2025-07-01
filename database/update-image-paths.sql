-- Обновление путей к изображениям с placeholder.svg на janome.webp
-- Для тестирования WebP функциональности

UPDATE projects 
SET image_url = '/images/projects/webp/janome.webp'
WHERE image_url = '/images/projects/placeholder.svg';

-- Проверка результата
SELECT 
    id,
    title,
    image_url
FROM projects 
ORDER BY id;

-- Показать количество обновленных записей
SELECT COUNT(*) as updated_projects 
FROM projects 
WHERE image_url = '/images/projects/webp/janome.webp'; 