@echo off
setlocal

REM Set variables
set DOCKER_USERNAME=cub1z
set IMAGE_NAME=webapp01
set IMAGE_TAG=latest
set DOCKER_HUB_REPO=%DOCKER_USERNAME%/%IMAGE_NAME%

REM Check if the image exists
docker image inspect %IMAGE_NAME%:%IMAGE_TAG% >nul 2>&1
if errorlevel 1 (
    echo ERROR: The image %IMAGE_NAME%:%IMAGE_TAG% does not exist!
    echo Run create_image.bat first to build the image.
    exit /b 1
)

REM Login to Docker Hub (will prompt for password)
echo Logging in to Docker Hub...
docker login -u %DOCKER_USERNAME%
if errorlevel 1 (
    echo ERROR: Failed to log in to Docker Hub!
    exit /b 1
)

REM Tag the image
echo Tagging image as %DOCKER_HUB_REPO%:%IMAGE_TAG%...
docker tag %IMAGE_NAME%:%IMAGE_TAG% %DOCKER_HUB_REPO%:%IMAGE_TAG%
if errorlevel 1 (
    echo ERROR: Failed to tag the image!
    exit /b 1
)

REM Push the image
echo Pushing image to Docker Hub...
docker push %DOCKER_HUB_REPO%:%IMAGE_TAG%
if errorlevel 1 (
    echo ERROR: Failed to push the image to Docker Hub!
    exit /b 1
)

echo Image published successfully to Docker Hub!
endlocal