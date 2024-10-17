 # Assemble Spring Boot executable JAR
./gradlew clean assemble

# Build the Docker image
docker build -t rentride-backend .

# Check if the container exists
if [ "$(docker ps -a -q -f name=rentride_be_staging)" ]; then
    # Stop the container if it's running
    docker stop rentride_be_staging
    # Remove the container
    docker rm rentride_be_staging
fi

# Run the Docker container in net sem3_network_staging, exposing port 8090
docker run -d --name rentride_be_staging -p 8090:8080 --network rentride_network --env SPRING_PROFILES_ACTIVE=staging rentride-backend