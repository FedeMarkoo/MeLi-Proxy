FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests=true

FROM openjdk:8-jdk-alpine

COPY target/meli-proxy-api-*.jar meli-proxy-api.jar

ENTRYPOINT ["java","-jar","/meli-proxy-api.jar"]
