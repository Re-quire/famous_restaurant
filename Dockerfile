FROM --platform=linux/amd64 openjdk:17-jdk-slim
ARG JAR_FILE=application/build/libs/yummy_server.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
