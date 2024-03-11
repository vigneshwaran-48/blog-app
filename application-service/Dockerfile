FROM openjdk:17-jdk-alpine

WORKDIR /opt/app

COPY target/application-service-0.0.1-SNAPSHOT.jar application-service.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=dev", "application-service.jar"]
