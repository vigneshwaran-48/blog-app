FROM openjdk:17-jdk-alpine

WORKDIR /opt/app

COPY target/static-service-0.0.1-SNAPSHOT.jar static-service.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker", "static-service.jar"]
