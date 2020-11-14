
FROM mysql:latest


ENV MYSQL_DATABASE=GottaEat
ENV MYSQL_ROOT_PASSWORD=Super$3cR3t
ENV MYSQL_USER=orbit
ENV MYSQL_PASSWORD=orbit

COPY schemas/setup.sql /docker-entrypoint-initdb.d/

