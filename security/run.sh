#!/bin/sh
# constants
export image_name=eterna1ity/simple-crud

# script build and push
./../gradlew :statefull:clean build
mkdir -p ./build/dependency
# shellcheck disable=SC2164
cd ./build/dependency
jar -xf ../libs/*.jar
cd ../..
docker build --build-arg DEPENDENCY=./build/dependency -t $image_name .
docker push $image_name