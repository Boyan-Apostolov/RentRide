echo "=========="
echo "[1/4]: Building Gradle Application"
echo "=========="

./gradlew clean assemble

echo "=========="
echo "[2/4]: Building Docker Image"
echo "=========="

docker build -t rentride-backend .

echo "=========="
echo "[3/4]: Updating image in hub.docker.com"
echo "=========="

docker tag rentride-backend bobby156/rentride-be
docker push bobby156/rentride-be

echo "=========="
echo "[4/4]: Started deployment to render.com"
echo "=========="

curl -X POST \
  -H "Authorization: Bearer rnd_eFXYJOxzO3Ao16PPAipkrJYn5OyT" \
  -H "Accept: application/json" \
  https://api.render.com/v1/services/srv-cte3hk5umphs739be5ig/deploys