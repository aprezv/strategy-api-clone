FROM openjdk:15-oracle

LABEL authors="Armando Perez <aprezv@gmail.com>"

ADD artifact/strategy-api.jar /app/api.jar

WORKDIR /app

CMD ["java", "-XX:MaxRAMPercentage=80.0", "-jar", "/app/api.jar"]
