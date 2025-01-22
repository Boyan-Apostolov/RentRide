echo "=========="
echo "[1/3]: Building Docker Image"
echo "=========="

docker build -t rentride-frontned .

echo "=========="
echo "[3/4]: Updating image in hub.docker.com"
echo "=========="

docker tag rentride-frontned bobby156/rentride-fe
docker push bobby156/rentride-fe

echo "=========="
echo "[3/3]: Started deployment to render.com"
echo "=========="
curl -X POST \
  -H "Authorization: Bearer rnd_eFXYJOxzO3Ao16PPAipkrJYn5OyT" \
  -H "Accept: application/json" \
  https://api.render.com/v1/services/srv-ctebbb3tq21c73bhmg6g/deploys