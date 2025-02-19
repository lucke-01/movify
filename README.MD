# Movify

## goal

evaluate skills in rest api, jpa, spring boot, desing etc..

## Features

### Cache with Ehcache

This application uses ehcache to cache

### Mappers with mapstruct

This application use mapstruct to map entities

### API code generation with OpenApi-codegen

This application use code generation,
to save time and to force compliance with the open api contract

### API documentation using open-api spec

I used the approach of API-FIRST

### API GUI using open-api swagger UI

This API is auto-documented using Swagger UI

### Database DDL and DML with versions using Liquibase

I have used liquibase to maintain database version
and to be agnostic to the type of database used

### Architecture - Hexagonal

I decided to use hexagonal architecture,
giving the app the possibility to adapt to any change

### Data access with JPA and Spring data JPA

I decided to use JPA and spring data JPA

### Database - SQL with h2 to operate in memory

I decided to use H2 to make this app runnable without a database server

## Requirements

JDK Java 21

Maven

## Use and installation

### configure open api generated resources in IDE

1. In your IDE go to target/generated-sources/open-api/src
2. Right click > mark directory as > generates resources root
3. You should be able to use this directory packages in your IDE

### configure lombok

1. Install lombok plugin in your IDE
2. depending on the IDE you will receive different instructions
    1. intellij: https://projectlombok.org/setup/intellij
    2. eclipse: https://projectlombok.org/setup/eclipse
3. Note some IDES may require some adjustment

### build application:

``` cmd
mvn clean install
```

### RUN application in IDE:

Run in IDE

``` cmd
com.ricardocreates.movify.MovifyApplication
```

### Run application with docker

TODO: build with docker

## USER GUIDE

### REST DOC - Swagger access

Open the following URL

``` browser
http://localhost:8080/swagger-ui
```

### H2 database manager

Open the following URL

``` browser
http://localhost:8080/h2-console
```

Introduce this as jdbc url:

``` input
jdbc:h2:mem:appDb
```