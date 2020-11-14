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

To quickly build and run all components you can use Docker. Make sure you have [Docker installed](https://www.docker.com/products/docker-desktop).
Then you have to choose one of the profiles from below and set the required environment values.
For example, this can be done by adding them in front of the start command or by adding them to the current
shell using `export KEY=VALUE`.
Then execute the following instructions inside the project directory:

```shell script
docker-compose up
```

On the first run the Docker images are build from the sources. This might take a while depending on your internet connection and processor speed.
Once everything has started you can visit the application on [localhost:2020](http://localhost:2020).

### Prod profile

The production profile will setup the database if it is missing. Otherwise, it will use the existing database. 
The following environment values are required:

```shell script
NOODLE_MAIL_HOST=<smtp host>
NOODLE_MAIL_PORT=<smtp port>
NOODLE_MAIL_USER=<smtp user>
NOODLE_MAIL_FROM=<smtp from email>
NOODLE_MAIL_PASSWORD=<smtp password>
NOODLE_SECURITY_EXPIRATION_TIME=<jwt token expiration>
NOODLE_SECURITY_SECRET=<jwt secret>
NOODLE_SECURITY_PEPPER=<pepper for passwords>
NOODLE_BASE_URL=<baseurl of noodle>
```

When a database created by the demo profile is used, the pepper must be set to `pepper`.

### Demo profile

The demo profile will delete an existing database and create a new one with demo data.
The following environment values are required:

```shell script
NOODLE_PROFILE=demo
NOODLE_MAIL_HOST=<smtp host>
NOODLE_MAIL_PORT=<smtp port>
NOODLE_MAIL_USER=<smtp user>
NOODLE_MAIL_FROM=<smtp from email>
NOODLE_MAIL_PASSWORD=<smtp password>
NOODLE_SECURITY_EXPIRATION_TIME=<jwt token expiration>
NOODLE_SECURITY_SECRET=<jwt secret>
NOODLE_BASE_URL=<baseurl of noodle>
```

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

To actually run the frontend _against the backend_ run `ng proxy` instead of `ng serve`. 

> Note: Live-Reload can lead to Errors in the Angular frontend.
> When encounter a problem after changing code, restart the application.

### Backend

On the backend, [Spring Boot](https://spring.io/projects/spring-boot) does the heavy lifting.
The build is done by [Gradle](https://gradle.org).

#### Setting up the Backend

You will need a Java15-JDK like
[this one](https://adoptopenjdk.net/?variant=openjdk15&jvmVariant=hotspot).

To make sure everything works right, follow the instructions in [Setting up the API](#setting-up-the-api) first.
Additionally, a development profile must be created and activated as described in [Development Profile](#development-profile).

Then, to set up and start the backend, open a terminal in its root directory and use the following command:

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

In the following sections, some useful application properties are listed.
Those can be added to `application.properties` or `application-dev.properties`.
If they are added to the `application.properties`, those changes must not be committed.
Some of the cofiguration options are required.

#### Mail Settings (required)

To allow sending mails (which is required for user registration) add the following:

 ```properties
 spring.mail.host=<smtp host>
 spring.mail.port=<port>
 spring.mail.username=<username, usually the email address>
 spring.noodle.mail.from=<FROM address>
 spring.mail.password=<password>
 ```

Optionally, a debug-flag can be added to get more details about the SMTP connection:

```properties
spring.mail.properties.mail.debug=true
```

The following security settings must be set. Make sure to set the hashPepper to `pepper` for the `data.sql` example data.

#### Security Settings (required)
Settings to make the application secure. Some of those must not be published.

```properties
spring.noodle.security.expirationTime=<expiration time>
spring.noodle.security.secret=<secret>
spring.noodle.security.hashPepper=<pepper>
```

#### BaseURL (required)
The baseurl used to access noodle. In local dev builds this is probably `http://localhost:8080` or `http://<ip>:8080`.

```properties
spring.noodle.baseurl=<baseurl of noodle>
```

#### Hibernate Debugging

With the following, you can see Hibernate's SQL-Queries for debugging.

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

#### Persistent Database

To write the database to a file add the following:

```properties
spring.datasource.url=jdbc:h2:file:/path/to/database
```

When a persistent database is enabled, the demo data config must be added as well.
This is described in the next section.

#### Demo Data Config

When importing the demo data (default behavior), the following should be
added so that the database is cleared before inserting the demo data.
```properties
spring.jpa.hibernate.ddl-auto=create
```

Alternatively, it is possible to disable the demo data:
```properties
spring.datasource.initialization-mode=never
```
