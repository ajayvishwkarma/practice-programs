FROM openjdk:11-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} demo2-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "java","-jar", "-Dserver.port=80", "demo2-0.0.1-SNAPSHOT.jar" ]