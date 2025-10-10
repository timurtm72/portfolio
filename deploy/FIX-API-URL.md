# Исправление проблемы с API URL на Orange Pi

## Проблема
Frontend пытается обратиться к `http://95.31.238.60:3000/api/projects` вместо относительного пути `/api`, что приводит к ошибке `ERR_CONNECTION_REFUSED`.

## Причина
Frontend был собран с `REACT_APP_API_URL=http://localhost:8080/api` вместо `/api` (относительный путь для Nginx прокси).

## Решение

### Шаг 1: Подключитесь к Orange Pi
```bash
ssh timur@orangepizero3
# или используйте ваш IP
ssh timur@95.31.238.60
```

### Шаг 2: Обновите код с GitHub
```bash
cd ~/portfolio
git pull origin main
```

### Шаг 3: Создайте правильный `.env` файл
```bash
cp frontend/env.example frontend/.env
```

Содержимое `frontend/.env`:
```bash
REACT_APP_API_URL=/api
```

### Шаг 4: Запустите скрипт исправления
```bash
chmod +x fix-frontend.sh
./fix-frontend.sh
```

**Или вручную:**
```bash
# Остановить frontend
docker compose stop frontend

# Пересобрать с новой конфигурацией (займет ~20-30 минут на Orange Pi)
docker compose build --no-cache frontend

# Запустить frontend
docker compose up -d frontend

# Проверить статус
docker compose ps
docker compose logs frontend
```

### Шаг 5: Проверьте работу
```bash
# Проверка API через frontend Nginx
curl http://localhost:3000/api/skills

# Проверка напрямую к backend
curl http://localhost:8080/api/skills
```

**В браузере:** http://95.31.238.60:3000

## Проверка конфигурации

### Правильная конфигурация Docker Nginx
В [frontend/Dockerfile](../frontend/Dockerfile#L33-L45):
```nginx
location /api/ {
    proxy_pass http://backend:8080/api/;
    # ... остальные заголовки
}
```

### Правильный API URL в React
В [frontend/src/config.ts](../frontend/src/config.ts):
```typescript
export const API_URL = process.env.REACT_APP_API_URL || '/api';
```

### Правильный .env для Docker
```bash
REACT_APP_API_URL=/api
```

## Ожидаемый результат
После пересборки frontend будет обращаться к `/api/projects`, а Nginx проксирует это на `http://backend:8080/api/projects`.

## Troubleshooting

### Проблема: Frontend все еще обращается к старому URL
**Решение:** Очистите кеш браузера или откройте в режиме инкогнито.

### Проблема: Пересборка занимает слишком много времени
**Решение:** Это нормально для ARM64. Первая сборка может занять до 30-60 минут.

### Проблема: Backend недоступен
**Решение:** Проверьте статус backend:
```bash
docker compose ps backend
docker compose logs backend
```

## Дополнительная информация
- GitHub репозиторий: https://github.com/timurtm72/portfolio
- Commit с исправлением: 98836798
