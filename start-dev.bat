@echo off
title Portfolio Development Environment

echo ========================================
echo  Портфолио Инженера-Программиста
echo  Запуск среды разработки
echo ========================================
echo.

:: Проверка зависимостей
echo Проверка установленных инструментов...

where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ОШИБКА] Java не найдена. Установите OpenJDK 17
    echo Скачать: https://adoptium.net/
    pause
    exit /b 1
)

where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [ОШИБКА] Maven не найден. Установите Apache Maven
    echo Скачать: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

where node >nul 2>nul
if %errorlevel% neq 0 (
    echo [ОШИБКА] Node.js не найден. Установите Node.js LTS
    echo Скачать: https://nodejs.org/
    pause
    exit /b 1
)

where psql >nul 2>nul
if %errorlevel% neq 0 (
    echo [ПРЕДУПРЕЖДЕНИЕ] PostgreSQL не найден в PATH
    echo Убедитесь, что PostgreSQL установлен и запущен
)

echo [OK] Все зависимости найдены
echo.

:: Проверка структуры проекта
if not exist "backend\pom.xml" (
    echo [ОШИБКА] Файл backend\pom.xml не найден
    echo Запустите скрипт из корневой директории проекта
    pause
    exit /b 1
)

if not exist "frontend\package.json" (
    echo [ОШИБКА] Файл frontend\package.json не найден
    echo Запустите скрипт из корневой директории проекта
    pause
    exit /b 1
)

echo [OK] Структура проекта корректна
echo.

:: Установка зависимостей frontend (если node_modules не существует)
if not exist "frontend\node_modules" (
    echo Установка зависимостей Frontend...
    cd frontend
    call npm install
    if %errorlevel% neq 0 (
        echo [ОШИБКА] Не удалось установить зависимости Frontend
        pause
        exit /b 1
    )
    cd ..
    echo [OK] Frontend зависимости установлены
    echo.
)

:: Сборка и тест Backend
echo Сборка Backend приложения...
cd backend
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo [ОШИБКА] Не удалось скомпилировать Backend
    echo Проверьте настройки базы данных в application.yml
    pause
    exit /b 1
)
cd ..
echo [OK] Backend скомпилирован успешно
echo.

:: Запуск приложений
echo ========================================
echo  ЗАПУСК ПРИЛОЖЕНИЙ
echo ========================================
echo.

echo Запуск Backend на порту 8080...
start "Portfolio Backend" cmd /c "cd backend && mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo Запуск Frontend на порту 3000...
start "Portfolio Frontend" cmd /c "cd frontend && npm start"

echo.
echo ========================================
echo  СРЕДА РАЗРАБОТКИ ЗАПУЩЕНА
echo ========================================
echo.
echo Frontend: http://localhost:3000
echo Backend API: http://localhost:8080/api
echo Swagger UI: http://localhost:8080/swagger-ui.html
echo Админ панель: http://localhost:3000/admin
echo.
echo Для остановки закройте окна терминалов
echo или нажмите Ctrl+C в каждом из них
echo.

pause 