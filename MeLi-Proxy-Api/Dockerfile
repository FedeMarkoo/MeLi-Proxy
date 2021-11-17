FROM maven:3.6.0-jdk-11-slim AS build

COPY MeLi-Proxy-Api/* /

RUN mvn -f pom.xml clean package -DskipTests=true

FROM openjdk:8-jdk-alpine

COPY target/meli-proxy-api-*.jar meli-proxy-api.jar

ENTRYPOINT ["java","-jar","/meli-proxy-api.jar"]
