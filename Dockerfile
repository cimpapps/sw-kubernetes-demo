FROM openjdk:8-jdk-alpine
WORKDIR /user/shared/app/

COPY target/*.jar app.jar
RUN ls
RUN apk add curl
RUN ls
RUN pwd
RUN java -version

ENTRYPOINT ["java", "-jar", "app.jar"]