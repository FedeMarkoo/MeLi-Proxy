FROM maven:3.6.0-jdk-11-slim AS build

COPY * /

RUN mvn -f /pom.xml clean package -DskipTests=true

FROM openjdk:8-jdk-alpine

COPY target/meli-proxy-manager-*.jar meli-proxy-manager.jar

ENTRYPOINT ["java", "-jar", "/meli-proxy-manager.jar"]
