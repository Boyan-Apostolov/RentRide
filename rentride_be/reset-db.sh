if [ "$(docker ps -a -q -f name=rentride_db)" ]; then
    # Stop the container if it's running
    docker stop rentride_db
    # Remove the container
    docker rm rentride_db
fi

docker run --name rentride_db -e MYSQL_DATABASE=rentride_db -e MYSQL_ROOT_PASSWORD=my-secret-pw -p 3306:3306 -d mysql
