#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
FROM apachepulsar/pulsar-standalone:latest

RUN apt-get update && apt-get install net-tools vim jq -y

#############################################################################
# Environment Variables
#############################################################################
ENV PULSAR_HOME=/pulsar

#############################################################################
# Use the modified configuration
#############################################################################
COPY conf/standalone.conf $PULSAR_HOME/conf/standalone.conf


#############################################################################
# Add all of our Pulsar Functions and IO Connectors
#############################################################################

COPY maven/*.jar /connectors/
COPY maven/*.yml /service-configs/

#############################################################################
# Add all automation scripts
#############################################################################
COPY scripts/*.sh /scripts/
RUN chmod a+x /scripts/*.sh

##############################################################################
# Start Pulsar in Standalone mode
##############################################################################
CMD ["/pulsar/bin/pulsar", "standalone"]

