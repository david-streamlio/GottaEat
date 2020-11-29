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

# Start from a Java image.
FROM openjdk:8

# Ignite version
ENV IGNITE_VERSION 2.9.0

# Ignite home
ENV IGNITE_HOME /opt/ignite/apache-ignite-${IGNITE_VERSION}-bin

# Do not rely on anything provided by base image(s), but be explicit, if they are installed already it is noop then
RUN apt-get update && apt-get install -y --no-install-recommends \
        unzip \
        curl \
    && rm -rf /var/lib/apt/lists/*
    
WORKDIR /opt/ignite

RUN curl https://dist.apache.org/repos/dist/release/ignite/${IGNITE_VERSION}/apache-ignite-${IGNITE_VERSION}-bin.zip -o ignite.zip \
    && unzip ignite.zip \
    && rm ignite.zip

#############################################################################
# Use the modified configuration
#############################################################################
COPY cfg/ignite-config.xml $IGNITE_HOME/config/ignite-config.xml

#############################################################################
# Add all of our Domain objects and dependencies on to the classpath. This is a kludge 
# until https://ignite.apache.org/docs/latest/code-deployment/deploying-user-code is fixed
#############################################################################
RUN mkdir /userLibs
COPY libs/*.jar /opt/ignite/apache-ignite-2.9.0-bin/libs/

#############################################################################
# Enable URI deployment
#############################################################################
RUN mv /opt/ignite/apache-ignite-2.9.0-bin/libs/optional/ignite-urideploy /opt/ignite/apache-ignite-2.9.0-bin/libs

#############################################################################
# Start Ignite IMDG
#############################################################################
CMD $IGNITE_HOME/bin/ignite.sh -J-XX:+UseG1GC -J-Xms2G -J-Xmx4G -J-XX:+DisableExplicitGC \
    -J-Djava.net.preferIPv4Stack=true $IGNITE_HOME/config/ignite-config.xml

EXPOSE 10800 11211 47100 47500 49112