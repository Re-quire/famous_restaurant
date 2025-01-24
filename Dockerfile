FROM openjdk:17-jdk
COPY application/build/libs/yummy_server.jar yummy_server.jar
ENTRYPOINT ["java", "-jar", "yummy_server.jar"]
