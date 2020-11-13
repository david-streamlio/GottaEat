FROM mariadb:latest

ENV MYSQL_ROOT_PASSWORD=Super$3cR3t
ENV MYSQL_DATABASE=GottaEat
ENV MYSQL_USER=orbit
ENV MYSQL_PASSWORD=orbit

RUN mkdir -p /tmp/psql_data/

COPY schemas/customer.sql /docker-entrypoint-initdb.d/
COPY schemas/order-history.sql /docker-entrypoint-initdb.d/
