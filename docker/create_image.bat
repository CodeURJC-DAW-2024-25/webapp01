@echo off

REM Set variables
SET IMAGE_NAME=webapp01
SET IMAGE_TAG=latest

REM Navigate to the project root directory
cd /d "%~dp0\.."

REM Build the Docker image
echo Building Docker image %IMAGE_NAME%:%IMAGE_TAG%...
docker build -t %IMAGE_NAME%:%IMAGE_TAG% -f docker/Dockerfile . || (
    echo ERROR: Docker build failed!
    exit /b 1
)

echo Docker image %IMAGE_NAME%:%IMAGE_TAG% built successfully!