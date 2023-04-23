#!/bin/bash

echo '### Java version ###'
java --version
echo '### Gradle version ###'
gradle --version

echo '### Stop services and remove old docker images ###'
docker-compose -f docker-compose.yml down
docker rmi -f $(docker images | grep 'sashkir7')

echo '### Build docker spring services ###'
bash ./gradlew clean build dockerTagLatest -x :rangiffler-test:test

echo '### Build docker frontend ###'
cd rangiffler-front || exit
bash ./docker-build.sh
cd ../ || exit

echo '### Show all docker images ###'
docker images

echo '### Start application docker containers ###'
docker-compose -f docker-compose.yml up -d

echo '### Show all containers  ###'
docker ps -a
