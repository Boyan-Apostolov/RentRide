echo "[1/4]: Building Gradle Application"

./gradlew clean assemble

echo "[2/4]: Building Docker Image"

docker build -t rentride-backend .

echo "[3/4]: Updating image in hub.docker.com"

docker tag rentride-backend bobby156/rentride-be
docker push bobby156/rentride-be

echo "[4/4]: Started deployment to render.com"

curl -X POST \
  -H "Authorization: Bearer rnd_eFXYJOxzO3Ao16PPAipkrJYn5OyT" \
  -H "Accept: application/json" \
  https://api.render.com/v1/services/srv-cte3hk5umphs739be5ig/deploys