FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/filafacil-1.0-SNAPSHOT.jar app.jar

RUN mkdir -p /app/data

ENV COLETAS_FILE_PATH=/app/data/coletas.txt

ENTRYPOINT ["java", "-jar", "app.jar"]