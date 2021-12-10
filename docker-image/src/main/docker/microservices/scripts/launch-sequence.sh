#!/bin/bash

/scripts/admin-init.sh

sleep 5

/scripts/start-simulators.sh

sleep 20

/pulsar/bin/pulsar-admin functions create --jar /connectors/order-validation-service-full.jar \
   --function-config-file /service-configs/order-validation-service-config.yml

# We need the above Function to publish to the persistent://orders/inbound/food-orders-meta topic 
# before we register this function as a consumer on the topic in order to ensure the schema type is AVRO

sleep 20

pulsar/bin/pulsar-admin functions create --jar /connectors/order-validation-service-full.jar  \
  --classname com.gottaeat.functions.ordervalidation.translator.OrderMetaAdapter \
  --inputs persistent://orders/inbound/food-orders-meta \
  --output persistent://orders/inbound/food-orders-aggregation \
  --tenant gottaeat --namespace services  \
  --schema-type AVRO

sleep 20
/pulsar/bin/pulsar-admin functions create --jar /connectors/order-validation-aggregator-service-full.jar \
  --function-config-file /service-configs/order-validation-aggregator-service-config.yml

sleep 20
/pulsar/bin/pulsar-admin functions create  --jar /connectors/payment-service-full.jar \
  --function-config-file /service-configs/payment-service-config.yml

sleep 20
/pulsar/bin/pulsar-admin functions create  --jar /connectors/credit-card-authorization-service-full.jar \
  --function-config-file /service-configs/credit-card-authorization-service-config.yml

sleep 20
/pulsar/bin/pulsar-admin functions create  --jar /connectors/order-validation-service-full.jar \
  --classname com.gottaeat.functions.ordervalidation.translator.PaymentAdapter \
   --inputs persistent://payments/inbound/processed \
   --output persistent://orders/inbound/food-orders-aggregation  \
   --tenant gottaeat --namespace services \
   --schema-type AVRO

sleep 20
/pulsar/bin/pulsar-admin functions create  --jar /connectors/geo-encoding-service-full.jar \
  --function-config-file /service-configs/geo-encoding-service-config.yml

# We need to wait for the geo-encoding-service to publich to the persistent://geography/google/circuit-breaker-requests
# before we create the next function in order to ensure that the schema type is AVRO and not JSON

sleep 20
/pulsar/bin/pulsar-admin functions create --jar /connectors/geo-encoding-service-full.jar \
  --classname  com.gottaeat.services.geoencoding.google.GoogleCircuitBreakerFunction \
  --function-config-file /service-configs/google-circuit-breaker-config.yml

sleep 20
/pulsar/bin/pulsar-admin functions create --jar /connectors/geo-encoding-service-full.jar \
  --classname  com.gottaeat.services.geoencoding.google.GoogleMapsFunction \
  --function-config-file /service-configs/google-maps-function-config.yml

sleep 20
/pulsar/bin/pulsar-admin functions create  --jar /connectors/geo-encoding-aggregator-service-full.jar \
   --function-config-file /service-configs/geo-encoding-aggregator-service-config.yml

sleep 20

/pulsar/bin/pulsar-admin functions create \
  --jar /connectors/order-validation-service-full.jar \
  --classname com.gottaeat.functions.ordervalidation.translator.AddressAdapter \
  --inputs persistent://geography/inbound/encoded \
  --output persistent://orders/inbound/food-orders-aggregation \
  --tenant gottaeat --namespace services \
  --schema-type ARVO
  
sleep 10
/pulsar/bin/pulsar-admin functions create \
  --jar /connectors/order-solicititation-service-full.jar \
  --function-config-file /service-configs/order-solicititation-service-config.yml
  
sleep 20
/pulsar/bin/pulsar-admin functions create \
  --jar /connectors/order-solicititation-aggregator-service-full.jar \
  --function-config-file /service-configs/order-solicititation-aggregator-service-config.yml
  
sleep 10

echo "Deploying restaurant simulator"

/pulsar/bin/pulsar-admin functions create \
 --jar /connectors/restaurant-simulator-full.jar \
 --function-config-file /service-configs/restaurant-simulator-config.yml
 

 /pulsar/bin/pulsar-admin functions create \
  --jar /connectors/order-validation-service-full.jar \
  --classname com.gottaeat.functions.ordervalidation.translator.FoodOrderAdapter \
  --inputs persistent://restaurants/inbound/assigned \
  --output persistent://orders/inbound/food-orders-aggregation \
  --tenant gottaeat --namespace services \
  --schema-type AVRO
  
