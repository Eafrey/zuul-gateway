#!/bin/bash

./gradlew clean bootRepackage

docker build --rm . --tag chensen/zuul-gateway:${VER:?invalid version}
docker push chensen/zuul-gateway:${VER:?invalid version}

export VER
docker stack deploy todo -c docker-compose.yml