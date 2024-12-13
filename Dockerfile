FROM --platform=linux/amd64 openjdk:21-jdk-slim as runtime

ENV JAVA_OPTS="-Dspring.profiles.active=production"

WORKDIR /opt/app
COPY ./build/libs/RentRide_BE-0.0.1-SNAPSHOT.jar ./

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar RentRide_BE-0.0.1-SNAPSHOT.jar"]