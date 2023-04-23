#!/bin/bash

bash ./gradlew :rangiffler-test:clean test
bash ./gradlew :rangiffler-test:allureServe
