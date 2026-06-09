FROM openjdk:26
# Crea directory per l'app
WORKDIR /app

# Copia il jar dell'applicazione
COPY target/*.jar app.jar

# Espone la porta 8080
EXPOSE 8080

# Avvia l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]