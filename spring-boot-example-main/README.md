# Spring Boot Example

`/GET /POST /PUT /DELETE` APIs for managing currencies.

## Tech stack

* Java 17
* Spring boot - CRUD operations for managing currencies; application exposed on port `8081`
* H2 database - for production and test code
* Swagger
* OpenApi
* Spring integration tests using `WebTestClient`
* Junit5, AssertJ
* Lombok
* Maven
* Docker

## Prerequisites

In order to build the project, you will have to install the following:

* Java 17
* Maven
* Docker (optional)
* This project includes **Lombok Annotations** (for IntelliJ IDEA).


## Build

### Maven

```
mvn clean install
```

### Docker

```
docker build -t currency-service .
```

## Run

### Maven

```
mvn spring-boot:run
```

### Docker

```
docker run -p 8081:8081 currency-service
```


## Swagger / OpenApi

Swagger endpoint: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

OpenApi endpoint: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

* log data
* swagger-ui
* i18n design
* Able to run on Docker 


