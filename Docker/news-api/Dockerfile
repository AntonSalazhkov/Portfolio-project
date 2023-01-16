FROM adoptopenjdk/openjdk11:ubi

ARG JAR_FILE=news-api-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY ${JAR_FILE} news-api.jar

ENTRYPOINT ["java", "-jar", "news-api.jar"]
