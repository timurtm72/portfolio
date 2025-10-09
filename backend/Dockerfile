# Multi-stage build для оптимизации размера образа
# Stage 1: Сборка (с поддержкой ARM64)
FROM amazoncorretto:17-alpine AS build

WORKDIR /app

# Копируем Maven wrapper и pom.xml для кеширования зависимостей
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Даем права на выполнение
RUN chmod +x mvnw

# Загружаем зависимости (этот слой кешируется пока не меняется pom.xml)
RUN ./mvnw dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime (с поддержкой ARM64)
FROM amazoncorretto:17-alpine

WORKDIR /app

# Создаем пользователя для безопасности
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Копируем JAR из build stage
COPY --from=build /app/target/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
