#!/bin/bash

SIMULATORS="customer-mobile-app-simulator"

for sim in $SIMULATORS
do
  echo "Deploying $sim"
  /pulsar/bin/pulsar-admin sources create \
     --archive /connectors/$sim-full.jar \
     --source-config-file /service-configs/$sim-config.yml
done