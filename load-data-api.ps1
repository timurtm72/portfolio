# PowerShell скрипт для загрузки данных портфолио через REST API
# Убедитесь, что backend запущен на http://localhost:8080

$baseUrl = "http://localhost:8080/api"

Write-Host "Загрузка данных портфолио через REST API..." -ForegroundColor Green

# Функция для отправки POST запроса
function Send-PostRequest {
    param(
        [string]$Url,
        [string]$Body
    )
    
    try {
        $response = Invoke-RestMethod -Uri $Url -Method Post -Body $Body -ContentType "application/json"
        return $response
    }
    catch {
        Write-Host "Ошибка при отправке запроса к $Url : $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# Навыки
Write-Host "Добавление навыков..." -ForegroundColor Yellow

$skills = @(
    @{
        name = "STM32"
        description = "Программирование ARM микроконтроллеров STM32 различных серий"
        level = 95
        category = "МИКРОКОНТРОЛЛЕРЫ"
        displayOrder = 1
    },
    @{
        name = "ESP32"
        description = "Разработка IoT устройств на ESP32 с ESP-IDF"
        level = 90
        category = "МИКРОКОНТРОЛЛЕРЫ"
        displayOrder = 2
    },
    @{
        name = "Omron PLC"
        description = "Программирование промышленных контроллеров Omron (SysMac Studio)"
        level = 90
        category = "ПЛК"
        displayOrder = 3
    },
    @{
        name = "Java Spring Boot"
        description = "Backend разработка на Java Spring Boot"
        level = 85
        category = "ПРОГРАММИРОВАНИЕ"
        displayOrder = 4
    },
    @{
        name = "C/C++"
        description = "Программирование микроконтроллеров на C/C++"
        level = 95
        category = "ПРОГРАММИРОВАНИЕ"
        displayOrder = 5
    }
)

foreach ($skill in $skills) {
    $json = $skill | ConvertTo-Json
    $result = Send-PostRequest -Url "$baseUrl/skills" -Body $json
    if ($result) {
        Write-Host "Добавлен навык: $($skill.name)" -ForegroundColor Green
    }
}

# Проекты
Write-Host "Добавление проектов..." -ForegroundColor Yellow

$projects = @(
    @{
        title = "Система контроля утечки газа"
        description = "Проектировал плату контроля утечки газа (метан, пропан, угарный газ) для автомобилей и бытового применения. Автомобильная версия включает три зоны контроля, а домашняя версия оснащена функцией автоматического переключения на аккумулятор."
        category = "АВТОМАТИЗАЦИЯ"
        status = "COMPLETED"
        isFeatured = $true
        displayOrder = 1
        githubUrl = "https://github.com/timurtm72?tab=repositories"
        technologies = @("STM32", "ESP32", "GSM SIM800", "WiFi", "Датчики газа", "C/C++")
    },
    @{
        title = "REST API для документооборота"
        description = "Разработал проект REST API для документооборота с использованием Spring Boot, PostgreSQL, JPA, Hibernate. Включает валидацию данных, работу с JSON, интеграцию со сторонним API."
        category = "WEB_РАЗРАБОТКА"
        status = "COMPLETED"
        isFeatured = $true
        displayOrder = 2
        githubUrl = "https://github.com/timurtm72?tab=repositories"
        technologies = @("Java", "Spring Boot", "PostgreSQL", "JPA", "Hibernate", "REST API")
    },
    @{
        title = "Система управления станцией перегонки нефти"
        description = "Проектировал и разрабатывал программы для ПЛК компании JUMO (Fulda) для автоматизации системы управления станцией перегонки нефти."
        category = "ПЛК"
        status = "COMPLETED"
        isFeatured = $true
        displayOrder = 3
        technologies = @("JUMO mTRON T", "ПЛК", "Автоматизация", "Нефтепереработка")
    }
)

foreach ($project in $projects) {
    $json = $project | ConvertTo-Json -Depth 3
    $result = Send-PostRequest -Url "$baseUrl/projects" -Body $json
    if ($result) {
        Write-Host "Добавлен проект: $($project.title)" -ForegroundColor Green
    }
}

# Опыт работы
Write-Host "Добавление опыта работы..." -ForegroundColor Yellow

$workExperiences = @(
    @{
        position = "Инженер-программист"
        company = "ООО «Проект 24»"
        location = "Иннополис"
        startDate = "2022-02-01"
        endDate = $null
        isCurrent = $true
        description = "Программирование промышленных контроллеров (SysMac Studio, CodeSys, SCADA, HMI). Разработка и отладка алгоритмов управления оборудованием. Написание ПО для встраиваемых систем на ESP32, STM32, RISC-V."
        technologies = "SysMac Studio, CodeSys, SCADA, HMI, ESP32, STM32, RISC-V, C/C++"
        achievements = "Успешная реализация проектов автоматизации, разработка надежных алгоритмов управления"
    },
    @{
        position = "Java-программист"
        company = "Maxima OOO"
        location = "Казань"
        startDate = "2022-11-01"
        endDate = "2023-06-30"
        isCurrent = $false
        description = "Разработал проект REST API для документооборота: Spring Boot, PostgreSQL, JPA, Hibernate. Валидация данных, работа с JSON. Интеграция со сторонним API."
        technologies = "Java, Spring Boot, PostgreSQL, JPA, Hibernate, REST API, Spring Security"
        achievements = "Успешно реализован полнофункциональный REST API для документооборота"
    },
    @{
        position = "Инженер программист микроконтроллеров"
        company = "ООО \"Водий нефтегазовик энергия\""
        location = "Ташкент"
        startDate = "2017-08-01"
        endDate = "2018-04-30"
        isCurrent = $false
        description = "Проектировал плату контроля утечки газа (метан, пропан, угарный газ) для автомобилей и бытового применения на языке С/С++."
        technologies = "STM32, ESP32, RISC-V, GSM SIM800, Wi-Fi, C/C++, датчики газа"
        achievements = "Разработана надежная система контроля утечки газа для автомобильного и бытового применения"
    }
)

foreach ($experience in $workExperiences) {
    $json = $experience | ConvertTo-Json -Depth 3
    $result = Send-PostRequest -Url "$baseUrl/work-experience" -Body $json
    if ($result) {
        Write-Host "Добавлен опыт работы: $($experience.position) в $($experience.company)" -ForegroundColor Green
    }
}

Write-Host "Загрузка данных завершена!" -ForegroundColor Green
Write-Host "Проверьте данные:" -ForegroundColor Cyan
Write-Host "- Навыки: http://localhost:8080/api/skills" -ForegroundColor Cyan
Write-Host "- Проекты: http://localhost:8080/api/projects" -ForegroundColor Cyan
Write-Host "- Опыт работы: http://localhost:8080/api/work-experience" -ForegroundColor Cyan 