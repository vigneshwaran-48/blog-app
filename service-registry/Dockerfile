FROM openjdk:17-jdk-alpine

WORKDIR /opt/app

COPY target/servie-registry-0.0.1-SNAPSHOT.jar service-registry.jar

ENTRYPOINT ["java","-jar","service-registry.jar"]
