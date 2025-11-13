FROM maven:3.9.9-sapmachine-22 AS build
COPY pom.xml /build/
WORKDIR /build/
COPY /libs/core-0.0.1-SNAPSHOT.jar /build/libs/
RUN mvn install:install-file \
        -Dfile=libs/core-0.0.1-SNAPSHOT.jar \
        -DgroupId=com.booksclub \
        -DartifactId=core \
        -Dversion=0.0.1-SNAPSHOT \
        -Dpackaging=jar \
    && mvn dependency:go-offline
COPY src /build/src
COPY target /build/target
RUN mvn package -DskipTests

FROM openjdk:22-jdk
ARG JAR_FILE=/build/target/*.jar
COPY --from=build $JAR_FILE /opt/booksclub/app.jar
ENTRYPOINT ["java", "-jar", "/opt/booksclub/app.jar"]