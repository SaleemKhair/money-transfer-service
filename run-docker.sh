#!/usr/bin/env bash

mvn clean package

APP_NAME="transfer-service"

docker build -t bsf/transfer-service .

docker run -p 8092:8092 bsf/transfer-service -e APP_NAME=$APP_NAME