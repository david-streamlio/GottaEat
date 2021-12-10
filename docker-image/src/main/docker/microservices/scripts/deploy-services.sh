#!/bin/bash

SERVICES="order-validation-service \
  order-validation-aggregator-service \
  geo-encoding-service \
  geo-encoding-aggregator-service \
  order-solicititation-service \
  order-solicititation-aggregator-service \
  payment-service \
  credit-card-authorization-service \
  paypal-authorization-service"

for svc in $SERVICES
do
  echo "Deploying $svc"
  /pulsar/bin/pulsar-admin functions create \
     --jar /connectors/$svc-full.jar \
     --function-config-file /service-configs/$svc-config.yml
done

#########################
# GeoEncoding
#########################

echo "Deploying GeoEncoding Service"

/pulsar/bin/pulsar-admin functions create --jar /connectors/geo-encoding-service-full.jar \
  --classname  com.gottaeat.services.geoencoding.google.GoogleCircuitBreakerFunction \
  --function-config-file /service-configs/google-circuit-breaker-config.yml

/pulsar/bin/pulsar-admin functions create --jar /connectors/geo-encoding-service-full.jar \
  --classname  com.gottaeat.services.geoencoding.google.GoogleMapsFunction \
  --function-config-file /service-configs/google-maps-function-config.yml
  

#########################
# Adapters
#########################

echo "Deploying Adapters"

/pulsar/bin/pulsar-admin functions create \
  --jar /connectors/order-validation-service-full.jar \
  --classname com.gottaeat.functions.ordervalidation.translator.AddressAdapter \
  --inputs persistent://geography/inbound/encoded \
  --output persistent://orders/inbound/food-orders-aggregation \
  --tenant gottaeat --namespace services \
  --schema-type AVRO
  
/pulsar/bin/pulsar-admin functions create \
  --jar /connectors/order-validation-service-full.jar \
  --classname com.gottaeat.functions.ordervalidation.translator.PaymentAdapter \
  --inputs persistent://payments/inbound/processed \
  --output persistent://orders/inbound/food-orders-aggregation \
  --tenant gottaeat --namespace services \
  --schema-type AVRO

/pulsar/bin/pulsar-admin functions create \
  --jar /connectors/order-validation-service-full.jar \
  --classname com.gottaeat.functions.ordervalidation.translator.OrderMetaAdapter \
  --inputs persistent://orders/inbound/food-orders-meta \
  --output persistent://orders/inbound/food-orders-aggregation \
  --tenant gottaeat --namespace services \
  --schema-type AVRO
  
 /pulsar/bin/pulsar-admin functions create \
  --jar /connectors/order-validation-service-full.jar \
  --classname com.gottaeat.functions.ordervalidation.translator.FoodOrderAdapter \
  --inputs persistent://restaurants/inbound/assigned \
  --output persistent://orders/inbound/food-orders-aggregation \
  --tenant gottaeat --namespace services \
  --schema-type AVRO