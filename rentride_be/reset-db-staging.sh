if [ "$(docker ps -a -q -f name=rentride_db_staging)" ]; then
    # Stop the container if it's running
    docker stop rentride_db_staging
    # Remove the container
    docker rm rentride_db_staging
fi

docker run --name rentride_db_staging -e MYSQL_DATABASE=rentride_db --network rentride_network -e MYSQL_ROOT_PASSWORD=my-secret-pw -p 3396:3306 -d mysql
