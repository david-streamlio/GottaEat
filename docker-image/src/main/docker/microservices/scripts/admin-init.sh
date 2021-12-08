#!/bin/bash

TENANTS="gottaeat orders geography payments restaurants"

for t in $TENANTS
do
   echo "Creating tenant $t"
   /pulsar/bin/pulsar-admin tenants create $t
done

NAMESPACES="gottaeat/services \
 gottaeat/simulators \
 geography/encoding \
 geography/inbound \
 geography/google \
 orders/inbound \
 payments/inbound \
 restaurants/inbound \
 restaurants/solicitations \
 restaurants/solicited "

for n in $NAMESPACES
do
  echo "Creating namespace $n"
  /pulsar/bin/pulsar-admin namespaces create $n

  echo "Setting retention policies on $n"
  /pulsar/bin/pulsar-admin namespaces set-retention --size 10M --time 1h $n
  
  echo "Setting security policies on $n"
  /pulsar/bin/pulsar-admin namespaces grant-permission --actions produce,consume --role admin $n
done


TOPICS="persistent://gottaeat/services/geo-encoding-service-control \
  persistent://geography/google/circuit-breaker-requests \
  persistent://geography/google/circuit-breaker-failures \
  persistent://geography/encoding/google \
  persistent://geography/encoding/aggregation \
  persistent://geography/inbound/non-encoded \
  persistent://geography/inbound/encoded-aggregate \
  persistent://orders/inbound/encoded \
  persistent://orders/inbound/valid-food-orders \
  persistent://orders/inbound/invalid-food-orders \
  persistent://orders/inbound/food-orders-meta \
  persistent://payments/inbound/pending \
  persistent://payments/inbound/applePay \
  persistent://payments/inbound/creditCard \
  persistent://payments/inbound/eCheck \
  persistent://payments/inbound/paypal \
  persistent://payments/inbound/authorized \
  persistent://restaurants/inbound/unassigned \
  persistent://restaurants/inbound/assigned \
  persistent://restaurants/solicited/accepted"

for t in $TOPICS
do
  echo "Creating topic $t"
  /pulsar/bin/pulsar-admin topics create $t
done

 
 
