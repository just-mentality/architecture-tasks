#!/bin/sh
# constants
export image_name=eterna1ity/spring-boot-docker

# script build and push
./gradlew clean build
mkdir -p ./build/dependency
cd ./build/dependency
jar -xf ../libs/*.jar
cd ../..
pwd
docker build --build-arg DEPENDENCY=./build/dependency -t $image_name .
docker push $image_name