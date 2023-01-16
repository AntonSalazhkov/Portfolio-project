FROM adoptopenjdk/openjdk8:alpine-jre

ARG JAR_FILE=settings-api-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY ${JAR_FILE} configserver.jar

ENTRYPOINT ["java", "-jar", "configserver.jar"]
