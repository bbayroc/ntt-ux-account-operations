FROM amazoncorretto:11-alpine-jdk
MAINTAINER bayro

COPY target/UX-Demo-0.0.1-SNAPSHOT.jar /UX-Demo.jar
ENTRYPOINT ["java","-jar","/UX-Demo.jar"]

#docker build -t UX-Demo-img .
#docker run -dp 8070:8070 UX-Demo-img

