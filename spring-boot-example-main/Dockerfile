FROM eclipse-temurin:17.0.5_8-jdk-focal

COPY target/currency-service-0.0.1-SNAPSHOT.jar currency-service.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","/currency-service.jar"]

