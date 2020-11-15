SET character_set_client = utf8mb4 ;

USE GottaEat;

DROP TABLE IF EXISTS franchise;

--
-- Table structure for table franchise
--

CREATE TABLE franchise (
  franchise_id SMALLINT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (franchise_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS restaurant;

--
-- Table structure for table restaurant
--

CREATE TABLE restaurant (
  restaurant_id SMALLINT NOT NULL AUTO_INCREMENT,
  franchise_id SMALLINT NOT NULL,
  address_id SMALLINT UNSIGNED NOT NULL,
  user_id SMALLINT UNSIGNED,
  PRIMARY KEY (restaurant_id),
  KEY idx_fk_franchise_id (franchise_id),
  KEY idx_fk_address_id (address_id),
  CONSTRAINT fk_restaurant_manager_user_id FOREIGN KEY (user_id) REFERENCES registered_user (user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_restaurant_address FOREIGN KEY (address_id) REFERENCES address (address_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table menu
--

DROP TABLE IF EXISTS menu;
 
CREATE TABLE menu (
  menu_id SMALLINT NOT NULL AUTO_INCREMENT,
  franchise_id SMALLINT NOT NULL,
  title varchar(75) COLLATE utf8mb4_unicode_ci NOT NULL,
  summary tinytext COLLATE utf8mb4_unicode_ci,
  createdAt datetime NOT NULL,
  updatedAt datetime DEFAULT NULL,
  content text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (menu_id),
  KEY idx_menu_franchise (franchise_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table item
--

DROP TABLE IF EXISTS item;

CREATE TABLE item (
  item_id INT NOT NULL AUTO_INCREMENT,
  title varchar(75) COLLATE utf8mb4_unicode_ci NOT NULL,
  summary tinytext COLLATE utf8mb4_unicode_ci,
  cooking tinyint(1) NOT NULL DEFAULT '0',
  sku varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  price float NOT NULL DEFAULT '0',
  quantity float NOT NULL DEFAULT '0',
  unit smallint(6) NOT NULL DEFAULT '0',
  instructions text COLLATE utf8mb4_unicode_ci,
  content text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table menu_item
--

DROP TABLE IF EXISTS menu_item;

CREATE TABLE menu_item (
  menu_item_id INT NOT NULL AUTO_INCREMENT,
  menu_id SMALLINT NOT NULL,
  item_id INT NOT NULL,
  active boolean NOT NULL,
  PRIMARY KEY (menu_item_id),
  UNIQUE KEY uq_menu_item (menu_id, item_id),
  KEY idx_menu_item_menu (menu_id),
  KEY idx_menu_item_item (item_id),
  CONSTRAINT fk_menu_item_item FOREIGN KEY (item_id) REFERENCES item (item_id) ON DELETE RESTRICT,
  CONSTRAINT fk_menu_item_menu FOREIGN KEY (menu_id) REFERENCES menu (menu_id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `order`;

--
-- Table structure for table order
--

CREATE TABLE `order` (
  order_id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL,
  status smallint(6) NOT NULL DEFAULT '0',
  subTotal float NOT NULL DEFAULT '0',
  itemDiscount float NOT NULL DEFAULT '0',
  tax float NOT NULL DEFAULT '0',
  shipping float NOT NULL DEFAULT '0',
  total float NOT NULL DEFAULT '0',
  promo varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  discount float NOT NULL DEFAULT '0',
  grandTotal float NOT NULL DEFAULT '0',
  delivery_address_id INT NOT NULL,
  createdAt datetime NOT NULL,
  updatedAt datetime DEFAULT NULL,
  content text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (order_id),
  KEY idx_order_customer (customer_id),
  CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES user (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS order_item;

--
-- Table structure for table order_item
--

CREATE TABLE order_item (
  order_item_id INT NOT NULL AUTO_INCREMENT,
  order_id INT NOT NULL,
  item_id INT NOT NULL,
  sku varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  price float NOT NULL DEFAULT '0',
  discount float NOT NULL DEFAULT '0',
  quantity float NOT NULL DEFAULT '0',
  unit smallint(6) NOT NULL DEFAULT '0',
  createdAt datetime NOT NULL,
  updatedAt datetime DEFAULT NULL,
  content text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (order_item_id),
  KEY idx_order_item_order (order_id),
  KEY idx_order_item_item (item_id),
  CONSTRAINT fk_order_item_item FOREIGN KEY (item_id) REFERENCES item (item_id),
  CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES `order` (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
