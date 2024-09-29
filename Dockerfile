FROM gradle:8.10-jdk21
WORKDIR /opt/app
COPY ./build/libs/RentRide_BE-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar RentRide_BE-0.0.1-SNAPSHOT.jar"]