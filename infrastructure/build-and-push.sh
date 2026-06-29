#!/usr/bin/env bash
set -e  # exit on first error

DOCKER_USER="maubersek"
VERSION="${1:-1.0}"   # default to 1.0, override: ./build-and-push.sh 1.1

SERVICES=("user-service" "vehicle-service" "booking-service")

echo "Building all services with Maven..."
./mvnw clean package -DskipTests

for service in "${SERVICES[@]}"; do
    IMAGE="${DOCKER_USER}/car-sharing-${service}:${VERSION}"
    echo ""
    echo "==> Building ${IMAGE}"
    docker build -f "${service}/src/main/docker/Dockerfile.jvm" \
                 -t "${IMAGE}" \
                 "${service}"
    echo "==> Pushing ${IMAGE}"
    docker push "${IMAGE}"
done

echo ""
echo "All images built and pushed (version: ${VERSION})"