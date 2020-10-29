# NAK-IAA
This Project is being developed as an examination for the 
"Internet Application Architectures" (Internetanwendungsarchitekturen) course of the 
[Nordakademie](https://www.nordakademie.de) in 2020.

It is being developed by [Til Blechschmidt](https://github.com/TilBlechschmidt), 
[Noah Peeters](https://github.com/NoahPeeters), [Hendrik Reiter](https://github.com/HenryWedge) 
and [Hans Rißer](https://github.com/hpr1999). 

The application at hand is a survey tool used to plan various meetings 
with multiple participants.

We have divided it into the [frontend](/frontend) and the [backend](backend).
Installation Instructions for developing can be found 
[here for the frontend](#setting-up-the-frontend) and 
[here for the backend](#setting-up-the-backend).

## Quick Start

To quickly build and run all components you can use Docker. Make sure you have [Docker installed](https://www.docker.com/products/docker-desktop),
then execute the following instructions inside the project directory:

```shell script
docker-compose up
```

On the first run the Docker images are build from the sources. This might take a while depending on your internet connection and processor speed.
Once everything has started you can visit the application on [localhost:2020](http://localhost:2020). 

## Development setup

### Setting up the API

This project uses an Open-API 3 specification.
To set up and generate the API access code in the frontend and the `api.yml` for the backend, 
clone the repository, open a terminal in its root directory and use the following instructions: 

```shell script
cd api
yarn install
yarn generate
```

### Frontend

The frontend of the application was built using [Angular](https://angular.io) 
and [yarn](https://classic.yarnpkg.com/en/).

#### Setting up the Frontend

For a start, you will need 
[yarn](https://classic.yarnpkg.com/en/docs/install/) and [Angular CLI](https://cli.angular.io).

To make sure everything works right, follow the instructions in [Setting up the API](#setting-up-the-api) first.

Then, to set up and start the frontend, clone the repository, 
open a terminal in its root directory and use the following instructions:

```shell script
cd frontend
yarn install
ng serve
```

### Backend

On the backend, [Spring Boot](https://spring.io/projects/spring-boot) does the heavy lifting.
The build is done by [Gradle](https://gradle.org).

#### Setting up the Backend

You will need a Java15-JDK like 
[this one](https://adoptopenjdk.net/?variant=openjdk15&jvmVariant=hotspot).

To make sure everything works right, follow the instructions in [Setting up the API](#setting-up-the-api) first.

Then, to set up and start the backend, clone the repository, 
open a terminal in its root directory and use the following command: 

```shell script
./gradlew bootRun
```

#### Development Profile

When developing the backend in IntelliJ, it's recommended to pass
it the environment variable `spring_profiles_active=dev` to enable
the development profile. 
Now, you can add a `application-dev.properties`
in [backend/src/main/resources](backend/src/main/resources) to change
Properties locally without conflicting with the checked in 
[application.properties](backend/src/main/resources/application.properties).

#### Hibernate Debugging

In order to see Hibernate's SQL-Queries when debugging, add the
following to your `application.properties` or, if you have one, your
 `application-dev.properties`. 

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```
