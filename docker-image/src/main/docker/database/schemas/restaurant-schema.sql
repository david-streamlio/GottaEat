SET character_set_client = utf8mb4 ;

USE GottaEat;

--
-- Table structure for table franchise
--
DROP TABLE IF EXISTS Franchise;

CREATE TABLE Franchise (
  franchise_id SMALLINT NOT NULL AUTO_INCREMENT,
  name varchar(75) NOT NULL,
  PRIMARY KEY (franchise_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `resturant manager`
--
DROP TABLE IF EXISTS RestaurantManager;

CREATE TABLE RestaurantManager (
  resturant_mgr_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY  (resturant_mgr_id),
  KEY idx_fk_manager_user_id (user_id),
  CONSTRAINT fk_manager_user_id FOREIGN KEY (resturant_mgr_id) REFERENCES RegisteredUser (user_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table restaurant
--
DROP TABLE IF EXISTS Restaurant;

CREATE TABLE Restaurant (
  restaurant_id SMALLINT NOT NULL AUTO_INCREMENT,
  franchise_id SMALLINT NOT NULL,
  address_id SMALLINT UNSIGNED NOT NULL,
  resturant_mgr_id SMALLINT UNSIGNED,
  PRIMARY KEY (restaurant_id),
  KEY idx_fk_franchise_id (franchise_id),
  KEY idx_fk_address_id (address_id),
  CONSTRAINT fk_restaurant_manager_user_id FOREIGN KEY (resturant_mgr_id) REFERENCES RestaurantManager (resturant_mgr_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_restaurant_address FOREIGN KEY (address_id) REFERENCES Address (address_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table menu
--

DROP TABLE IF EXISTS Menu;

CREATE TABLE Menu (
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

DROP TABLE IF EXISTS Item;

CREATE TABLE Item (
  item_id INT NOT NULL AUTO_INCREMENT,
  title varchar(75) COLLATE utf8mb4_unicode_ci NOT NULL,
  summary tinytext COLLATE utf8mb4_unicode_ci,
  sku varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  price float NOT NULL DEFAULT '0',
  content text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table menu_item
--

DROP TABLE IF EXISTS MenuItem;

CREATE TABLE MenuItem (
  menu_item_id INT NOT NULL AUTO_INCREMENT,
  menu_id SMALLINT NOT NULL,
  item_id INT NOT NULL,
  active boolean NOT NULL,
  PRIMARY KEY (menu_item_id),
  UNIQUE KEY uq_menu_item (menu_id, item_id),
  KEY idx_menu_item_menu (menu_id),
  KEY idx_menu_item_item (item_id),
  CONSTRAINT fk_menu_item_item FOREIGN KEY (item_id) REFERENCES Item (item_id) ON DELETE RESTRICT,
  CONSTRAINT fk_menu_item_menu FOREIGN KEY (menu_id) REFERENCES Menu (menu_id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `DeliveryStatusCode`
--
DROP TABLE IF EXISTS OrderStatusCode;

CREATE TABLE OrderStatusCode (
  status_code SMALLINT NOT NULL, 
  description varchar(50),
  PRIMARY KEY (status_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table order
--
DROP TABLE IF EXISTS `Order`;

CREATE TABLE `Order` (
  order_id INT NOT NULL AUTO_INCREMENT,
  status smallint(6) NOT NULL DEFAULT '0',
  subTotal float NOT NULL DEFAULT '0',
  itemDiscount float NOT NULL DEFAULT '0',
  tax float NOT NULL DEFAULT '0',
  total float NOT NULL DEFAULT '0',
  promo varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  discount float NOT NULL DEFAULT '0',
  grandTotal float NOT NULL DEFAULT '0',
  createdAt datetime NOT NULL,
  updatedAt datetime DEFAULT NULL,
  content text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (order_id),
  CONSTRAINT fk_order_status FOREIGN KEY (status) REFERENCES OrderStatusCode (status_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table order_item
--
DROP TABLE IF EXISTS OrderItem;

CREATE TABLE OrderItem (
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
  CONSTRAINT fk_order_item_item FOREIGN KEY (item_id) REFERENCES Item (item_id),
  CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES `Order` (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
