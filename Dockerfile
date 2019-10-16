FROM openjdk:8-jdk-alpine
MAINTAINER serv izerui <liuyuhua69@gmail.com>
VOLUME /tmp
COPY target/open-learn-guides-1.2.2-SNAPSHOT-exec.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]