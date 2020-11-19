USE GottaEat;

--
-- Table structure for table `Devices`
--
CREATE TABLE RegisteredDevice (
  device_id VARCHAR(50) DEFAULT NULL,
  user_id SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY  (device_id),
  CONSTRAINT fk_device_user_id FOREIGN KEY (user_id) REFERENCES RegisteredUser (user_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Table strucuture for table `RegisteredUser`
-- 
CREATE TABLE RegisteredUser (
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

CREATE TABLE Customer (
  customer_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id SMALLINT UNSIGNED NOT NULL,
  address_id SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY  (customer_id),
  KEY idx_fk_customer_user_id (user_id),
  KEY idx_fk_address_id (address_id),
  CONSTRAINT fk_customer_user_id FOREIGN KEY (user_id) REFERENCES RegisteredUser (user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_customer_address FOREIGN KEY (address_id) REFERENCES Address (address_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

