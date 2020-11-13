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

COPY maven/ /connectors 

##############################################################################
# Start Pulsar in Standalone mode
##############################################################################
CMD ["/pulsar/bin/pulsar", "standalone"]

