SET NAMES utf8mb4;
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS GottaEat;
CREATE SCHEMA GottaEat;
USE GottaEat;


SOURCE /tmp/schemas/locations-schema.sql
SOURCE /tmp/schemas/locations-data.sql
SOURCE /tmp/schemas/users-schema.sql
SOURCE /tmp/schemas/users-data.sql
SOURCE /tmp/schemas/resturant-schema.sql


