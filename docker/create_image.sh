#!/bin/bash

# Set variables
IMAGE_NAME="webapp01"
IMAGE_TAG="latest"

# Navigate to the project root directory
cd "$(dirname "$0")/.."

# Build the Docker image
echo "Building Docker image ${IMAGE_NAME}:${IMAGE_TAG}..."
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -f docker/Dockerfile . || {
    echo "ERROR: Docker build failed!"
    exit 1
}

echo "Docker image ${IMAGE_NAME}:${IMAGE_TAG} built successfully!"