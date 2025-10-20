FROM maven:3.9.9-sapmachine-22 AS build
COPY pom.xml /build/
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src
COPY target /build/target
RUN mvn package -DskipTests

FROM openjdk:22
ARG JAR_FILE=/build/target/*.jar
COPY --from=build $JAR_FILE /opt/booksclub/app.jar
ENTRYPOINT ["java", "-jar", "/opt/booksclub/app.jar"]