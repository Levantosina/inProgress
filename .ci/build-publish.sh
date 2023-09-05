:"${USERNAME:?USERNAME not set}"
:"${PASSWORD:?PASSWORD not set}"
:"${TAG:?TAG not set}"

docker buildx create --use

docker buildx build \
    --platform= linux/amd64,linux/arm64 \
    -t "$USERNAME/REPO:$TAG" \
    -t "$USERNAME/REPO:latest" \
    "${@:2}" \
    --push
    "$1"

