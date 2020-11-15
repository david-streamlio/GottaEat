SET AUTOCOMMIT=0;

USE GottaEat;

--
-- Create some order statuses
--

INSERT INTO OrderStatusCode VALUES (0, 'New'),
(1, 'Validated'),
(2, 'Approved'),
(3, 'Assigned To Restaurant'),
(4, 'Ready For Pickup'),
(5, 'In Route To Customer'),
(6, 'Delivered'),
(7, 'Cancelled');
COMMIT;


--
-- Create some franchises
--

INSERT INTO Franchise VALUES (1, "McDonalds"), 
(2, "Taco Bell"),
(3, "Chick-fil-a");
COMMIT;

--
-- Create some resturants for the franchises 
-- 

INSERT INTO Restaurant VALUES (1, 1, 30, 30),
(2, 1, 31, 31),
(3, 1, 32, 32),
(4, 2, 33, 33),
(5, 2, 34, 34),
(6, 3, 35, 35),
(7, 3, 36, 36);
COMMIT;

--
-- Create some menus for each franchise
--

INSERT INTO Menu VALUES (1, 1, 'Breakfast Menu', '', '1970-10-14 22:04:36', '2020-10-14 22:04:36', 'Available from 6am to 11am'),
(2, 1, 'Lunch/Dinner Menu', '', '1970-10-14 22:04:36', '2020-10-14 22:04:36', 'Available after 11am'),
(3, 2, 'Breakfast Menu', '', '1970-10-14 22:04:36', '2020-10-14 22:04:36', 'Available from 6am to 11am'),
(4, 2, 'Lunch/Dinner Menu', '', '1970-10-14 22:04:36', '2020-10-14 22:04:36', 'Available after 11am'),
(5, 3, 'Breakfast Menu', '', '1970-10-14 22:04:36', '2020-10-14 22:04:36', 'Available from 6am to 11am'),
(6, 3, 'Lunch/Dinner Menu', '', '1970-10-14 22:04:36', '2020-10-14 22:04:36', 'Available after 11am');
COMMIT;

--
-- Create some food items
--

INSERT INTO Item VALUES (1, 'Big Mac', 'Two all beef patties', 'sku', 3.99, ''),
(2, 'Quarter Pounder with Cheese', '1/4 pound of fresh beef grilled', '', 3.79, ''),
(3, 'French Fries', '', '', 1.79, ''),
(4, 'Chicken Mcnuggets', '6 piece', '', 2.50, ''),
(5, 'Chicken Mcnuggets', '10 piece', '', 3.49, ''),
(6, 'Chicken Mcnuggets', '20 piece', '', 5.00, ''),
(7, 'Soft Drink', 'Small', '',  1.00, ''),
(8, 'Soft Drink', 'Medium', '', 1.00, ''),
(9, 'Soft Drink', 'Large', '', 1.00, ''),
(10, 'Original Nachos', 'Fresh corn tortilla chips smothered with refried pinto beans, cheddar and pepper jack cheeses, zesty enchilada sauce and black beans. All of this is topped with sour cream, salsa fresca and guacamole.', '', 5.29, ''),
(11, 'Cheese Quesadilla', 'Melted cheddar and pepper jack cheese grilled to melted perfection on a home-style tortilla.', '', 6.29, ''),
(12, 'Chicken And Black Bean Burrito', 'A home-style tortilla filled with black beans, cheddar cheese, seasoned rice, salsa fresca, sour cream, guacamole and all-white chicken.', '', 6.24, ''),
(13, 'Chicken Soft Taco', 'A soft tortilla filled with chicken, cheddar cheese, shredded lettuce and fresh diced tomato.', '', 1.39, ''),
(14, 'Soft Drink', 'Small', '',  1.00, ''),
(15, 'Soft Drink', 'Medium', '', 1.79, ''),
(16, 'Soft Drink', 'Large', '', 2.09,  ''),
(17, 'Chicken Sandwich',  '', '', 4.99, ''),
(18, 'Chick-n-Strips', '', '', 4.99, ''),
(19, 'Cobb Salad',  '', '', 7.99, ''),
(20, 'Waffle Potato Fries', '', '', 2.99, ''),
(21, 'Soft Drink',  '', '', 1.99, ''),
(22, 'Lemonade', '', '', 2.99, '');
COMMIT;

--
-- Add the food items to their respective menus
--

INSERT INTO MenuItem VALUES (1, 2, 1, TRUE),
(2, 2, 2, TRUE),
(3, 2, 3, TRUE),
(4, 2, 4, TRUE),
(5, 2, 5, TRUE),
(6, 2, 6, TRUE),
(7, 2, 7, TRUE),
(8, 2, 8, TRUE),
(9, 2, 9, TRUE),
(10, 4, 10, TRUE),
(11, 4, 11, TRUE),
(12, 4, 12, TRUE),
(13, 4, 13, TRUE),
(14, 4, 14, TRUE),
(15, 4, 15, TRUE),
(16, 4, 16, TRUE),
(17, 6, 17, TRUE),
(18, 6, 18, TRUE),
(19, 6, 19, TRUE),
(20, 6, 20, TRUE),
(21, 6, 21, TRUE),
(22, 6, 22, TRUE);
COMMIT;



