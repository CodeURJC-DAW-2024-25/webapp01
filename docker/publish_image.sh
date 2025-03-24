#!/bin/bash

# Set variables
DOCKER_USERNAME="cub1z"
IMAGE_NAME="webapp01"
IMAGE_TAG="latest"
DOCKER_HUB_REPO="${DOCKER_USERNAME}/${IMAGE_NAME}"

# Check if the image exists
if ! docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} &> /dev/null; then
    echo "ERROR: The image ${IMAGE_NAME}:${IMAGE_TAG} does not exist!"
    echo "Run create_image.sh first to build the image."
    exit 1
fi

# Login to Docker Hub (will prompt for password)
echo "Logging in to Docker Hub..."
docker login -u ${DOCKER_USERNAME} || {
    echo "ERROR: Failed to log in to Docker Hub!"
    exit 1
}

# Tag the image
echo "Tagging image as ${DOCKER_HUB_REPO}:${IMAGE_TAG}..."
docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${DOCKER_HUB_REPO}:${IMAGE_TAG} || {
    echo "ERROR: Failed to tag the image!"
    exit 1
}

# Push the image
echo "Pushing image to Docker Hub..."
docker push ${DOCKER_HUB_REPO}:${IMAGE_TAG} || {
    echo "ERROR: Failed to push the image to Docker Hub!"
    exit 1
}

echo "Image published successfully to Docker Hub!"