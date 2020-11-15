SET AUTOCOMMIT=0;

USE GottaEat;

--
-- Create some delivery statuses
--

INSERT INTO DeliveryStatusCode VALUES (0, 'Assigned a Driver'),
(1, 'In Route To Customer'),
(2, 'Delivered');
COMMIT;


--
-- Create some drivers
--

INSERT INTO Driver VALUES (11, 11, 11),
(12, 12, 12),
(13, 13, 13),
(14, 14, 14),
(15, 15, 15),
(16, 16, 16),
(17, 17, 17),
(18, 18, 18),
(19, 19, 19),
(20, 20, 20);
COMMIT;