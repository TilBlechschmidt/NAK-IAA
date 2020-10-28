# ----------------------- API builder -----------------------

FROM node:15.0.1-alpine3.10 AS api-builder

WORKDIR /api

# Cache any dependencies
COPY ./api/package.json ./api/yarn.lock ./
RUN yarn

# Generate the API
COPY ./api .
RUN yarn generate

# ----------------------- Java builder -----------------------

FROM adoptopenjdk:15_36-jdk-openj9-0.22.0-bionic AS java-builder

WORKDIR /project

# Cache gradle installation
COPY ./backend/build.gradle ./backend/settings.gradle ./backend/gradlew ./backend/
COPY ./backend/gradle ./backend/gradle
RUN cd backend && ./gradlew --no-daemon -v || return 0

# Build and bundle the backend jar
COPY . .
COPY --from=api-builder /api/target ./api/target
RUN cd backend && ./gradlew --no-daemon assemble

# ----------------------- Final image -----------------------

FROM adoptopenjdk:15_36-jre-openj9-0.22.0-bionic AS iaa-backend

COPY --from=java-builder /project/backend/build/libs/noodle-*.jar /
RUN mv /noodle-*.jar /noodle.jar

EXPOSE 8080
CMD java -jar /noodle.jar
