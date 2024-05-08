FROM openjdk:17-jdk-slim

COPY target/currency-service-0.0.1-SNAPSHOT.jar currency-service.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","/currency-service.jar"]

