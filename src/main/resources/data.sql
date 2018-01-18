INSERT INTO VENUE (NAME) VALUES ('BATMAN'),('SUPERMAN'), ('THE HULK');

INSERT INTO CUSTOMER (EMAIL) VALUES ('test@test.com');

INSERT INTO SEAT (COLUMN, ROW, VENUE_ID, SEAT_RESERVED_ID, SEAT_HOLD_ID) VALUES
('0','0','1',NULL,NULL),
('0','1','1',NULL,NULL),
('0','2','1',NULL,NULL),
('0','3','1',NULL,NULL),
('0','4','1',NULL,NULL),
('0','5','1',NULL,NULL),
('1','0','1',NULL,NULL),
('1','1','1',NULL,NULL),
('1','2','1',NULL,NULL),
('1','3','1',NULL,NULL),
('1','4','1',NULL,NULL),
('1','5','1',NULL,NULL),
('2','0','1',NULL,NULL),
('2','1','1',NULL,NULL),
('2','2','1',NULL,NULL),
('2','3','1',NULL,NULL),
('2','4','1',NULL,NULL),
('2','5','1',NULL,NULL),
('3','0','1',NULL,NULL),
('3','1','1',NULL,NULL),
('3','2','1',NULL,NULL),
('3','3','1',NULL,NULL),
('3','4','1',NULL,NULL),
('3','5','1',NULL,NULL),
('4','0','1',NULL,NULL),
('4','1','1',NULL,NULL),
('4','2','1',NULL,NULL),
('4','3','1',NULL,NULL),
('4','4','1',NULL,NULL),
('4','5','1',NULL,NULL),
('5','0','1',NULL,NULL),
('5','1','1',NULL,NULL),
('5','2','1',NULL,NULL),
('5','3','1',NULL,NULL),
('5','4','1',NULL,NULL),
('5','5','1',NULL,NULL),


('0','0','2',NULL,NULL),
('0','1','2',NULL,NULL),
('0','2','2',NULL,NULL),
('0','3','2',NULL,NULL),

('1','0','2',NULL,NULL),
('1','1','2',NULL,NULL),
('1','2','2',NULL,NULL),
('1','3','2',NULL,NULL),

('2','0','2',NULL,NULL),
('2','1','2',NULL,NULL),
('2','2','2',NULL,NULL),
('2','3','2',NULL,NULL),

('3','0','2',NULL,NULL),
('3','1','2',NULL,NULL),
('3','2','2',NULL,NULL),
('3','3','2',NULL,NULL),


('0','0','3',NULL,NULL),
('0','1','3',NULL,NULL),
('0','2','3',NULL,NULL),
('0','3','3',NULL,NULL),
('0','4','3',NULL,NULL),

('1','0','3',NULL,NULL),
('1','1','3',NULL,NULL),
('1','2','3',NULL,NULL),
('1','3','3',NULL,NULL),
('1','4','3',NULL,NULL),

('2','0','3',NULL,NULL),
('2','1','3',NULL,NULL),
('2','2','3',NULL,NULL),
('2','3','3',NULL,NULL),
('2','4','3',NULL,NULL),

('3','0','3',NULL,NULL),
('3','1','3',NULL,NULL),
('3','2','3',NULL,NULL),
('3','3','3',NULL,NULL),
('3','4','3',NULL,NULL),

('4','0','3',NULL,NULL),
('4','1','3',NULL,NULL),
('4','2','3',NULL,NULL),
('4','3','3',NULL,NULL),
('4','4','3',NULL,NULL);


-- ('2', '2', '3', NULL, SELECT ID FROM SEAT_GROUP_HOLD),
-- ('2', '2', '2', NULL, SELECT ID FROM SEAT_GROUP_HOLD),
-- ('2', '2', '1', NULL, SELECT ID FROM SEAT_GROUP_HOLD),
-- ('2', '2', '1', NULL, SELECT ID FROM SEAT_GROUP_HOLD),
-- ('2', '2', '2', NULL, SELECT ID FROM SEAT_GROUP_HOLD);
-- ('2', '2', '3', SELECT ID FROM SEAT_GROUP_RESERVED, NULL); --something wrong, dunno why?
