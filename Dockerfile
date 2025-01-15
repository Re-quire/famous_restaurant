FROM openjdk:17
ARG JAR_FILE=application/build/libs/yummy_server.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]