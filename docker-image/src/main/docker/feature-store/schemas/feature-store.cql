
CREATE KEYSPACE IF NOT EXISTS featurestore WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE featurestore.customer (customerID BIGINT,
   avg_order_spend DOUBLE, 
   percent_orders_to_home_addr DOUBLE, 
   PRIMARY KEY (customerID));

INSERT INTO featurestore.customer (customerid, avg_order_spend, percent_orders_to_home_addr) VALUES (1, 17.99, 100.0);
INSERT INTO featurestore.customer (customerid, avg_order_spend, percent_orders_to_home_addr) VALUES (2, 34.56, 75.0);
INSERT INTO featurestore.customer (customerid, avg_order_spend, percent_orders_to_home_addr) VALUES (3, 8.99, 5.0);

CREATE TABLE featurestore.restaurant (restaurantID BIGINT, 
    avg_meal_prep_time_last_7_days DOUBLE, 
    avg_meal_prep_time_last_hour DOUBLE,
    meals_prepared_last_hour INT,
    PRIMARY KEY (restaurantID));

INSERT INTO featurestore.restaurant (restaurantID, avg_meal_prep_time_last_7_days, avg_meal_prep_time_last_hour, meals_prepared_last_hour) 
  VALUES (1, 18, 22, 0);
INSERT INTO featurestore.restaurant (restaurantID, avg_meal_prep_time_last_7_days, avg_meal_prep_time_last_hour, meals_prepared_last_hour) 
  VALUES (2, 5, 4, 0);
