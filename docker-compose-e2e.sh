#!/bin/bash
source ./rangiffler-test/docker.properties

echo '### Java version ###'
java --version
echo '### Gradle version ###'
gradle --version

echo '### Stop services and remove old docker images ###'
docker-compose -f docker-compose.test.yml down
docker rmi -f $(docker images | grep 'akireev')

echo '### Build docker spring services ###'
bash ./gradlew clean build dockerTagLatest -x :rangiffler-test:test

echo '### Build docker frontend ###'
cd rangiffler-front || exit
bash ./docker-build.sh
cd ../ || exit

echo '### Build docker e2e-tests image ###'
ARCH=$(uname -m)
if [ "$ARCH" = "arm64" ] || [ "$ARCH" = "aarch64" ]; then
  docker build --build-arg DOCKER=arm64v8/eclipse-temurin:17-jdk -t akireev/test:${VERSION} -t akireev/test:latest -f ./rangiffler-test/Dockerfile .
else
  docker build --build-arg DOCKER=eclipse-temurin:17-jdk -t akireev/test:${VERSION} -t akireev/test:latest -f ./rangiffler-test/Dockerfile .
fi

echo '### Show all docker images ###'
docker images

echo '### Start application docker containers ###'
docker-compose -f docker-compose.test.yml up -d

echo '### Show all containers  ###'
docker ps -a
