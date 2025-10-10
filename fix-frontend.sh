#!/bin/bash
# Скрипт исправления конфигурации frontend на Orange Pi

echo "=== Создание правильного .env файла ==="
cat > ~/portfolio/frontend/.env << 'EOF'
# Frontend environment variables для Docker деплоя
# Используем относительный путь для работы через Nginx прокси
REACT_APP_API_URL=/api
EOF

echo "Создан файл frontend/.env:"
cat ~/portfolio/frontend/.env

echo -e "\n=== Остановка frontend контейнера ==="
cd ~/portfolio
docker compose stop frontend

echo -e "\n=== Пересборка frontend с новой конфигурацией ==="
docker compose build --no-cache frontend

echo -e "\n=== Запуск frontend ==="
docker compose up -d frontend

echo -e "\n=== Ожидание запуска (10 секунд) ==="
sleep 10

echo -e "\n=== Проверка статуса ==="
docker compose ps frontend

echo -e "\n=== Проверка доступности API через frontend ==="
curl -s http://localhost:3000/api/skills | head -n 5

echo -e "\n✅ Готово! Проверьте сайт в браузере: http://95.31.238.60:3000"
