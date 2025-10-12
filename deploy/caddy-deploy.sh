#!/bin/bash

################################################################################
# Скрипт автоматического деплоя портфолио с Caddy Reverse Proxy
#
# Использование:
#   На Orange Pi: bash deploy/caddy-deploy.sh
#   Из GitHub: git pull && bash deploy/caddy-deploy.sh
#
# Что делает:
#   1. Обновляет код из GitHub
#   2. Проверяет конфигурацию
#   3. Пересобирает Docker контейнеры
#   4. Запускает приложение с Caddy
#   5. Проверяет работоспособность
################################################################################

set -e  # Остановка при ошибке

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Функции вывода
info() {
    echo -e "${BLUE}ℹ ${1}${NC}"
}

success() {
    echo -e "${GREEN}✓ ${1}${NC}"
}

warning() {
    echo -e "${YELLOW}⚠ ${1}${NC}"
}

error() {
    echo -e "${RED}✗ ${1}${NC}"
}

# Проверка прав root (не требуется, но полезно)
if [ "$EUID" -eq 0 ]; then
    warning "Не рекомендуется запускать от root. Используйте обычного пользователя."
fi

################################################################################
# 1. Проверка окружения
################################################################################

info "Проверка окружения..."

# Проверка Docker
if ! command -v docker &> /dev/null; then
    error "Docker не установлен!"
    exit 1
fi

# Проверка Docker Compose
if ! command -v docker compose &> /dev/null; then
    error "Docker Compose не установлен!"
    exit 1
fi

success "Docker и Docker Compose установлены"

# Проверка что мы в правильной директории
if [ ! -f "docker-compose.yml" ]; then
    error "docker-compose.yml не найден! Запускайте скрипт из корня проекта."
    exit 1
fi

if [ ! -f "Caddyfile" ]; then
    error "Caddyfile не найден! Убедитесь что файл существует."
    exit 1
fi

success "Конфигурационные файлы найдены"

################################################################################
# 2. Обновление из GitHub
################################################################################

info "Обновление кода из GitHub..."

# Проверка что это Git репозиторий
if [ -d ".git" ]; then
    # Сохранить локальные изменения .env файлов
    if [ -f "backend/.env" ]; then
        cp backend/.env backend/.env.backup
        info "Резервная копия backend/.env создана"
    fi

    if [ -f "frontend/.env" ]; then
        cp frontend/.env frontend/.env.backup
        info "Резервная копия frontend/.env создана"
    fi

    # Обновить из GitHub
    git fetch origin

    # Проверить что на ветке main
    CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
    if [ "$CURRENT_BRANCH" != "main" ]; then
        warning "Вы на ветке $CURRENT_BRANCH, переключаемся на main..."
        git checkout main
    fi

    # Получить обновления
    LOCAL_HASH=$(git rev-parse HEAD)
    REMOTE_HASH=$(git rev-parse origin/main)

    if [ "$LOCAL_HASH" != "$REMOTE_HASH" ]; then
        info "Найдены обновления, применяем..."
        git pull origin main
        success "Код обновлен из GitHub"
    else
        success "Код актуален, обновлений не требуется"
    fi

    # Восстановить .env файлы
    if [ -f "backend/.env.backup" ]; then
        mv backend/.env.backup backend/.env
        success "backend/.env восстановлен"
    fi

    if [ -f "frontend/.env.backup" ]; then
        mv frontend/.env.backup frontend/.env
        success "frontend/.env восстановлен"
    fi
else
    warning "Не Git репозиторий, пропускаем обновление из GitHub"
fi

################################################################################
# 3. Проверка конфигурации
################################################################################

info "Проверка конфигурации..."

# Проверить что .env файлы существуют
if [ ! -f "backend/.env" ]; then
    warning "backend/.env не найден!"
    if [ -f "backend/env.example" ]; then
        info "Создаем backend/.env из env.example..."
        cp backend/env.example backend/.env
        warning "⚠️ ВАЖНО! Отредактируйте backend/.env и установите надежные пароли!"
        echo ""
        read -p "Нажмите Enter после редактирования backend/.env..."
    else
        error "backend/env.example не найден!"
        exit 1
    fi
fi

if [ ! -f "frontend/.env" ]; then
    warning "frontend/.env не найден!"
    if [ -f "frontend/env.example" ]; then
        info "Создаем frontend/.env из env.example..."
        cp frontend/env.example frontend/.env
    fi
fi

success "Файлы окружения проверены"

# Проверить что Caddyfile содержит домен (не localhost)
if grep -q "portfolio.timurtm72.ru" Caddyfile; then
    warning "⚠️ В Caddyfile используется домен по умолчанию: portfolio.timurtm72.ru"
    echo ""
    echo "Замените его на ваш реальный домен в файле Caddyfile!"
    echo "Пример: your-domain.com"
    echo ""
    read -p "Продолжить с текущим доменом? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        info "Отредактируйте Caddyfile и запустите скрипт снова"
        exit 0
    fi
fi

################################################################################
# 4. Проверка портов
################################################################################

info "Проверка портов..."

# Проверить что порты 80 и 443 свободны или заняты Docker
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        local process=$(lsof -Pi :$port -sTCP:LISTEN -t | head -1)
        local pname=$(ps -p $process -o comm= 2>/dev/null || echo "unknown")

        if [[ $pname == *"docker"* ]] || [[ $pname == *"containerd"* ]]; then
            success "Порт $port занят Docker (это нормально)"
        else
            warning "Порт $port занят процессом: $pname (PID: $process)"
            read -p "Продолжить? Может произойти конфликт портов. (y/N): " -n 1 -r
            echo
            if [[ ! $REPLY =~ ^[Yy]$ ]]; then
                exit 1
            fi
        fi
    else
        success "Порт $port свободен"
    fi
}

check_port 80
check_port 443

################################################################################
# 5. Остановка старых контейнеров
################################################################################

info "Остановка старых контейнеров..."

if docker compose ps | grep -q "Up"; then
    docker compose down
    success "Старые контейнеры остановлены"
else
    info "Контейнеры не запущены"
fi

################################################################################
# 6. Сборка и запуск
################################################################################

info "Сборка Docker образов..."
echo "⏳ Это может занять 30-60 минут на Orange Pi..."

# Показать прогресс
docker compose build --progress=plain 2>&1 | tee build.log

if [ $? -ne 0 ]; then
    error "Ошибка при сборке Docker образов!"
    error "Смотрите build.log для деталей"
    exit 1
fi

success "Docker образы собраны"

info "Запуск контейнеров..."

docker compose up -d

if [ $? -ne 0 ]; then
    error "Ошибка при запуске контейнеров!"
    exit 1
fi

success "Контейнеры запущены"

################################################################################
# 7. Проверка работоспособности
################################################################################

info "Ожидание запуска сервисов..."

# Функция ожидания контейнера
wait_for_container() {
    local container=$1
    local max_wait=$2
    local waited=0

    info "Ожидание запуска $container..."

    while [ $waited -lt $max_wait ]; do
        if docker compose ps | grep "$container" | grep -q "healthy"; then
            success "$container запущен и healthy"
            return 0
        fi

        if docker compose ps | grep "$container" | grep -q "Up"; then
            # Контейнер работает, но нет healthcheck или еще не healthy
            sleep 5
            waited=$((waited + 5))
        else
            error "$container не запущен!"
            return 1
        fi
    done

    warning "$container не стал healthy за $max_wait секунд"
    return 1
}

# Ждем базу данных
wait_for_container "portfolio-db" 60

# Ждем backend
wait_for_container "portfolio-backend" 120

# Ждем frontend
sleep 10  # Frontend обычно запускается быстро
if docker compose ps | grep "portfolio-frontend" | grep -q "Up"; then
    success "portfolio-frontend запущен"
else
    error "portfolio-frontend не запущен!"
    exit 1
fi

# Ждем Caddy
wait_for_container "portfolio-caddy" 30

################################################################################
# 8. Финальная проверка
################################################################################

info "Финальная проверка..."

# Проверить HTTP доступность
if curl -s -o /dev/null -w "%{http_code}" http://localhost | grep -q "200\|301\|302"; then
    success "HTTP сервер отвечает"
else
    warning "HTTP сервер не отвечает на localhost"
fi

# Показать статус
echo ""
info "Статус контейнеров:"
docker compose ps

# Показать логи Caddy (последние 20 строк)
echo ""
info "Последние логи Caddy:"
docker compose logs --tail=20 caddy

################################################################################
# 9. Итоговая информация
################################################################################

echo ""
echo "════════════════════════════════════════════════════════════════"
success "Деплой завершен успешно! 🚀"
echo "════════════════════════════════════════════════════════════════"
echo ""

# Получить домен из Caddyfile
DOMAIN=$(grep -oP '^\S+\s+\{' Caddyfile | head -1 | sed 's/ {//g' | sed 's/http:\/\///g')

if [ -n "$DOMAIN" ]; then
    echo "📍 Приложение доступно по адресу:"
    echo "   https://$DOMAIN"
    echo ""
fi

echo "📊 Полезные команды:"
echo "   docker compose ps          - статус контейнеров"
echo "   docker compose logs -f     - логи всех сервисов"
echo "   docker compose logs -f caddy  - логи Caddy"
echo "   docker compose restart     - перезапуск"
echo "   docker compose down        - остановка"
echo ""

# Проверить SSL сертификат
if [ -n "$DOMAIN" ] && [[ "$DOMAIN" != "localhost"* ]] && [[ "$DOMAIN" != *"."*"."* ]]; then
    info "SSL сертификат:"
    if docker exec portfolio-caddy ls /data/caddy/certificates 2>/dev/null | grep -q "$DOMAIN"; then
        success "SSL сертификат для $DOMAIN получен"
    else
        warning "SSL сертификат еще не получен"
        info "Caddy автоматически получит его при первом запросе к домену"
        info "Убедитесь что DNS настроен правильно!"
    fi
fi

echo ""
echo "🔒 Защита включена:"
echo "   ✓ Автоматический HTTPS (Let's Encrypt)"
echo "   ✓ Rate Limiting (100 запросов/минуту)"
echo "   ✓ Security Headers"
echo "   ✓ Порты 3000/8080/5432 закрыты от интернета"
echo "   ✓ Блокировка ботов и сканеров"
echo ""

warning "⚠️ Не забудьте:"
echo "   1. Закрыть старые порты в firewall: 3000, 8080, 5432"
echo "   2. Открыть порты 80 и 443 в firewall"
echo "   3. Настроить DNS A-запись на IP сервера"
echo ""

success "Готово! 🎉"
