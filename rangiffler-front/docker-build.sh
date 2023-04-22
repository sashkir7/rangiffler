#!/bin/bash
source ./docker.properties

echo '### Build test frontend image ###'
docker build --build-arg NPM_COMMAND=build:test -t akireev/frontend:0.0.1 -t akireev/frontend:latest .
