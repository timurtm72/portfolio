# PowerShell скрипт для загрузки данных портфолио
# Убедитесь, что PostgreSQL запущен и бэкенд Spring Boot работает

Write-Host "Загрузка данных портфолио Султанова Тимура..." -ForegroundColor Green

# Проверяем доступность PostgreSQL (предполагаем стандартные настройки)
$postgresHost = "localhost"
$postgresPort = "5432"
$postgresDb = "portfolio"
$postgresUser = "postgres"

# Путь к SQL файлу
$sqlFile = "database/updated-portfolio-data.sql"

# Проверяем существование SQL файла
if (-not (Test-Path $sqlFile)) {
    Write-Host "Ошибка: SQL файл не найден: $sqlFile" -ForegroundColor Red
    exit 1
}

Write-Host "Найден SQL файл: $sqlFile" -ForegroundColor Yellow

# Попытка выполнить SQL через psql (если установлен)
try {
    Write-Host "Попытка подключения к PostgreSQL..." -ForegroundColor Yellow
    
    # Проверяем наличие psql
    $psqlPath = Get-Command psql -ErrorAction SilentlyContinue
    
    if ($psqlPath) {
        Write-Host "Найден psql, выполняем SQL скрипт..." -ForegroundColor Green
        
        # Выполняем SQL скрипт
        & psql -h $postgresHost -p $postgresPort -d $postgresDb -U $postgresUser -f $sqlFile
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Данные успешно загружены!" -ForegroundColor Green
        } else {
            Write-Host "❌ Ошибка при выполнении SQL скрипта" -ForegroundColor Red
        }
    } else {
        Write-Host "psql не найден. Попробуйте один из вариантов:" -ForegroundColor Yellow
        Write-Host "1. Установите PostgreSQL клиент" -ForegroundColor White
        Write-Host "2. Используйте pgAdmin для выполнения SQL скрипта" -ForegroundColor White
        Write-Host "3. Скопируйте содержимое файла $sqlFile в ваш SQL клиент" -ForegroundColor White
    }
} catch {
    Write-Host "Ошибка подключения: $_" -ForegroundColor Red
    Write-Host "Попробуйте выполнить SQL скрипт вручную через pgAdmin или другой SQL клиент" -ForegroundColor Yellow
}

# Проверяем работу backend API
Write-Host "`nПроверяем API endpoints..." -ForegroundColor Yellow

try {
    $skillsResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/skills" -Method GET -TimeoutSec 5
    Write-Host "✅ API /skills работает, найдено навыков: $($skillsResponse.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ API /skills недоступен: $_" -ForegroundColor Red
}

try {
    $projectsResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/projects" -Method GET -TimeoutSec 5
    Write-Host "✅ API /projects работает, найдено проектов: $($projectsResponse.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ API /projects недоступен: $_" -ForegroundColor Red
}

try {
    $experienceResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/work-experience" -Method GET -TimeoutSec 5
    Write-Host "✅ API /work-experience работает, найдено записей: $($experienceResponse.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ API /work-experience недоступен: $_" -ForegroundColor Red
}

Write-Host "`nИнструкции по загрузке данных:" -ForegroundColor Cyan
Write-Host "1. Убедитесь, что PostgreSQL запущен" -ForegroundColor White
Write-Host "2. Убедитесь, что Spring Boot backend запущен (http://localhost:8080)" -ForegroundColor White
Write-Host "3. Выполните SQL скрипт: database/updated-portfolio-data.sql" -ForegroundColor White
Write-Host "4. Проверьте frontend на http://localhost:3000" -ForegroundColor White

Read-Host "`nНажмите Enter для завершения" 