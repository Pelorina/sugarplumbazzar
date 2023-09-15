# FROM gradle:7.3.3-jdk17 AS build
# WORKDIR /home/gradle/project
# FROM openjdk:17-jdk-alpine
# WORKDIR /app
# COPY ./sugar-plum-bazzar/build/libs/*.jar app.jar
# EXPOSE 8080

FROM openjdk:17-jdk-alpine
ARG JAR-FILE=sugar-plum-bazzar/build/*.jar
COPY ./sugar-plum-bazzar/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app.jar"]