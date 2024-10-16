# Spring Boot Starter Project

Initial project setup for Spring Boot Web-Application using Thymeleaf.

## Recommended Development Environment

   * IntelliJ
   * VSCode+Java
   * Eclipse
   * [Maven] optional - local maven project setup


## Basic Project Setup

   * Spring Boot Starter Web
   * Spring Boot Starter Tomcat
   * Spring Boot Starter Thymeleaf
   * Bootstrap templates
   * H2 database for persisting application data

### how to make your data persitent across spring-boot restarts

   * By default application data is stored in in-memory database
   * to persist your data, the following line within application.properties need to be changed from
      * myapp.datasource.url=jdbc:h2:mem:myappdb;
   * to
      * myapp.datasource.jdbcurl=jdbc:h2:file:/path2databasefile;

## IDE
### IntelliJ
When using the Community Version it can happen, that a Breakpoint will not hit. Someone found a solution for this:


```spring-boot:run -Dspring-boot.run.fork=false```

source: https://dev.to/davey/debugging-springboot-application-in-intellij-idea-ce-22j9

## Deployment

### Build
```bash
mvn package
```

Either deploy generated WAR File to Tomcat or directly run using
```bash
mvn spring-boot:run

### or alternatively build and run in one command
mvn package spring-boot:run

```
### Connect
localhost:8080

### REST API
To query the rest API use curl, httpie (https://httpie.io/), ...
Provide the Bearer token for authentication.

```
curl http://localhost:8080/api/users -H 'Accept: application/json' -H "Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2NjQzMDc0NDksImV4cCI6MTY5NTg0MzQ0OSwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoiYWRtaW4iLCJ1c2VybmFtZSI6ImFkbWluIn0.curjpEf0q9S43s5EPLB9Pk7VXZEex0onsK2xr74QOak"
```

## initialization code
e.g. for creating entities for testing, as the configured h2 database is in-memory only.
```
at.ac.fhsalzburg.swd.spring.startup.CommandLineAppStartupRunner.run
```

## Authentication
During startup an administration user is created with username=__admin__ and password=__admin__.
* All pages directly under root / do not need authentication
* All pages under /admin/ need an authenticated user with ADMIN role
* All pages under /user or /api need an authenticated user with either ADMIN or USER role
* Two authentication methods are implemented:
    * form based authentication
    * JWT Bearer authentication (especially for REST)

## connect to local database using h2-console (browser)
```
http://localhost:8080/h2-console
```
