#!/bin/bash
# Скрипт проверки статуса Docker контейнеров на Orange Pi

echo "=== Статус Docker контейнеров ==="
docker compose ps

echo -e "\n=== Проверка сети ==="
docker network inspect portfolio-network | grep -A 3 "Containers"

echo -e "\n=== Логи Backend (последние 20 строк) ==="
docker compose logs --tail=20 backend

echo -e "\n=== Проверка доступности Backend изнутри Frontend ==="
docker compose exec frontend wget -qO- http://backend:8080/api/actuator/health 2>&1

echo -e "\n=== Проверка доступности Backend с хоста ==="
curl -s http://localhost:8080/api/actuator/health

echo -e "\n=== Healthcheck Backend ==="
docker inspect portfolio-backend --format='{{.State.Health.Status}}'
