# Используем официальный образ OpenJDK 17 для выполнения приложения
FROM eclipse-temurin:17-jre

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный JAR-файл из локальной машины в контейнер
COPY target/*.jar app.jar

# Копируем конфигурационный файл, создаём папку config заранее
RUN mkdir -p ./config
COPY src/main/resources/application.yaml ./config/

# Открываем порт 8080 для приложения
EXPOSE 8080

# Запускаем приложение с указанием пути к конфигурационному файлу
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/config/application.yaml"]

