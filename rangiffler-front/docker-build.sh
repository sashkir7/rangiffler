#!/bin/bash
source ./docker.properties

echo '### Build docker frontend image ###'
docker build --build-arg NPM_COMMAND=build:docker -t akireev/frontend:${VERSION} -t akireev/frontend:latest .
