FROM eclipse-temurin:25-jdk-alpine AS build
ARG SKIP_TESTS=true
WORKDIR /app

COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml
RUN chmod +x mvnw

RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src src
RUN if [ "$SKIP_TESTS" = "true" ]; then \
      ./mvnw -q -DskipTests package; \
    else \
      ./mvnw -q package; \
    fi

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/spaceagency-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
