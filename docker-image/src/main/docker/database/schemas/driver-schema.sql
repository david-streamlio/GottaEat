SET character_set_client = utf8mb4 ;

USE GottaEat;

--
-- Table structure for table `driver`
--
DROP TABLE IF EXISTS Driver;

CREATE TABLE Driver (
  driver_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id BIGINT UNSIGNED NOT NULL,
  address_id BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (driver_id),
  KEY idx_fk_driver_user_id (user_id),
  CONSTRAINT fk_driver_user_id FOREIGN KEY (user_id) REFERENCES registered_user (user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_driver_address FOREIGN KEY (address_id) REFERENCES address (address_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `DeliveryStatusCode`
--
DROP TABLE IF EXISTS DeliveryStatusCode;

CREATE TABLE DeliveryStatusCode (
  status_code SMALLINT NOT NULL, 
  description varchar(50),
  PRIMARY KEY (status_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
  

--
-- Table structure for table `delivery`
--

DROP TABLE IF EXISTS Delivery;

CREATE TABLE Delivery (
  delivery_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  order_id INT NOT NULL,
  restaurant_id BIGINT NOT NULL,
  customer_id BIGINT NOT NULL,
  driver_id BIGINT UNSIGNED,
  delivery_address_id BIGINT UNSIGNED NOT NULL,
  status smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY  (delivery_id),
  CONSTRAINT fk_delivery_status FOREIGN KEY (status) REFERENCES DeliveryStatusCode (status_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

