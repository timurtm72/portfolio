# Multi-stage build для оптимизации
# Stage 1: Сборка React приложения (Debian для совместимости с ARM64)
FROM node:18 AS build

WORKDIR /app

# Копируем package files для кеширования зависимостей
COPY package*.json ./

# Устанавливаем зависимости (включая dev для сборки)
RUN npm install

# Копируем исходный код
COPY . ./

# Сборка production версии
RUN npm run build

# Stage 2: Nginx для раздачи статики
FROM nginx:alpine

# Копируем собранное приложение
COPY --from=build /app/build /usr/share/nginx/html

# Копируем конфигурацию Nginx для SPA routing
RUN echo 'server { \
    listen 80; \
    resolver 127.0.0.11 valid=30s ipv6=off; \
    set $backend_upstream backend:8080; \
    root /usr/share/nginx/html; \
    index index.html; \
    location / { \
        try_files $uri $uri/ /index.html; \
    } \
    location /api/ { \
        proxy_pass http://$backend_upstream/api/; \
        proxy_http_version 1.1; \
        proxy_set_header Upgrade $http_upgrade; \
        proxy_set_header Connection "upgrade"; \
        proxy_set_header Host $host; \
        proxy_set_header X-Real-IP $remote_addr; \
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; \
        proxy_set_header X-Forwarded-Proto $scheme; \
        proxy_connect_timeout 300s; \
        proxy_send_timeout 300s; \
        proxy_read_timeout 300s; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
