USE GottaEat;

--
-- Table strucuture for table `user`
-- 
CREATE TABLE registered_user (
  user_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  email VARCHAR(50) DEFAULT NULL,
  passwordHash VARCHAR(50) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  create_date DATETIME NOT NULL,
  last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY  (user_id),
  UNIQUE KEY uq_registered_user (email),
  KEY idx_last_name (last_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Table structure for table `customer`
--

CREATE TABLE customer (
  customer_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id SMALLINT UNSIGNED NOT NULL,
  address_id SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY  (customer_id),
  KEY idx_fk_customer_user_id (user_id),
  KEY idx_fk_address_id (address_id),
  CONSTRAINT fk_customer_user_id FOREIGN KEY (user_id) REFERENCES registered_user (user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_customer_address FOREIGN KEY (address_id) REFERENCES address (address_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `driver`
--

CREATE TABLE driver (
  driver_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id SMALLINT UNSIGNED NOT NULL,
  address_id SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY  (driver_id),
  KEY idx_fk_driver_user_id (user_id),
  CONSTRAINT fk_driver_user_id FOREIGN KEY (user_id) REFERENCES registered_user (user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_driver_address FOREIGN KEY (address_id) REFERENCES address (address_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Table structure for table `resturant manager`
--
CREATE TABLE manager (
  resturant_mgr_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY  (resturant_mgr_id),
  KEY idx_fk_manager_user_id (user_id),
  CONSTRAINT fk_manager_user_id FOREIGN KEY (user_id) REFERENCES registered_user (user_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

