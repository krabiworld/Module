FROM gradle:8.5-jdk21-alpine AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build --no-daemon

FROM openjdk:21-jre-alpine
COPY --from=build /app/build/libs/Module.jar /Module.jar
ENTRYPOINT ["java", "-jar", "/Module.jar"]
