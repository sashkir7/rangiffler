#!/bin/bash
source ./docker.properties

echo '### Build docker frontend image ###'
docker build --build-arg NPM_COMMAND=build:docker -t sashkir7/rangiffler-frontend:${VERSION} -t sashkir7/rangiffler-frontend:latest .
