FROM maven:3.6.3-openjdk-14-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B package --file pom.xml -DskipTests

FROM openjdk:8-jdk-alpine

COPY workspace/target/meli-proxy-api-*.jar meli-proxy-api.jar

ENTRYPOINT ["java","-jar","/meli-proxy-api.jar"]
