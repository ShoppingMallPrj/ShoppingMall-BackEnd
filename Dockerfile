FROM openjdk:8-jdk
ARG JAR_FILE=build/libs/*.jar
RUN echo $(ls -al)
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]