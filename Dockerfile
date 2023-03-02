FROM openjdk:15-alpine

LABEL authors="Armando Perez <aprezv@gmail.com>"

ADD target/strategy-api-*.jar /app/strategy-api.jar

WORKDIR /app

CMD ["java","-XX:MaxRAMPercentage=80.0", "-jar", "/app/strategy-api.jar"]
