-- Rebuilds the database from scratch with sample data.
-- WARNING: the DROP below deletes everything in newsPaperSystemDatabase.
DROP DATABASE IF EXISTS newsPaperSystemDatabase;
CREATE DATABASE newsPaperSystemDatabase;
USE newsPaperSystemDatabase;

CREATE TABLE newsPaperSystemDatabase.deliveryAreas (
    deliveryAreaId INT AUTO_INCREMENT PRIMARY KEY,
    location       VARCHAR(150) NOT NULL
);

CREATE TABLE newsPaperSystemDatabase.customers (
    customerId     INT AUTO_INCREMENT PRIMARY KEY,
    deliveryAreaId INT          NOT NULL,
    name           VARCHAR(150) NOT NULL,
    address        VARCHAR(150) NOT NULL,
    contact        VARCHAR(150) NOT NULL,
    FOREIGN KEY (deliveryAreaId) REFERENCES deliveryAreas(deliveryAreaId)
);

CREATE TABLE newsPaperSystemDatabase.publications (
    publicationId INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(150) NOT NULL,
    type  VARCHAR(150) NOT NULL,
    cost  INT          NOT NULL,
    stock INT          NOT NULL
);

CREATE TABLE newsPaperSystemDatabase.orders (
    orderId       INT AUTO_INCREMENT PRIMARY KEY,
    publicationId INT          NOT NULL,
    customerId    INT          NOT NULL,
    pattern        VARCHAR(150) NOT NULL,
    FOREIGN KEY (publicationId) REFERENCES publications(publicationId),
    FOREIGN KEY (customerId)    REFERENCES customers(customerId)
);

CREATE TABLE newsPaperSystemDatabase.deliveryDockets (
    deliveryDocketId INT AUTO_INCREMENT PRIMARY KEY,
    orderId         INT     NOT NULL,
    customerId      INT     NOT NULL,
    date            DATE    NOT NULL,
    success         BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (orderId)    REFERENCES orders(orderId),
    FOREIGN KEY (customerId) REFERENCES customers(customerId)
);

CREATE TABLE newsPaperSystemDatabase.users (
    userId       INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(150) NOT NULL,
    password     VARCHAR(150) NOT NULL,
    contact      VARCHAR(150) NOT NULL,
    access_level VARCHAR(150) NOT NULL
);

CREATE TABLE newsPaperSystemDatabase.bills (
    billId     INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT  NOT NULL,
    amount     INT  NOT NULL,
    date       DATE NOT NULL,
    FOREIGN KEY (customerId) REFERENCES customers(customerId)
);

-- ===================== SAMPLE DATA =====================

INSERT INTO deliveryAreas (location) VALUES
    ('Athlone Town Centre'),   -- 1
    ('Athlone East'),          -- 2
    ('Moate');                 -- 3

-- contact must be 7-11 digits (Customer.verifyContact)
INSERT INTO customers (deliveryAreaId, name, address, contact) VALUES
    (1, 'Mary Murphy',     '12 Church Street, Athlone',    '0871234567'),  -- 1
    (1, 'John Kelly',      '4 Connaught Street, Athlone',  '0862345678'),  -- 2
    (1, 'Siobhan Byrne',   '27 Irishtown, Athlone',        '0853456789'),  -- 3
    (2, 'Patrick Walsh',   '8 Retreat Road, Athlone',      '0874567890'),  -- 4
    (2, 'Aoife Ryan',      '15 Ballymahon Road, Athlone',  '0835678901'),  -- 5
    (2, 'Liam O''Brien',   '3 Golden Island, Athlone',     '0896789012'),  -- 6
    (3, 'Niamh Doyle',     '21 Main Street, Moate',        '0867890123'),  -- 7
    (3, 'Sean Gallagher',  '6 Station Road, Moate',        '0878901234');  -- 8

-- cost is a whole euro amount; stock below 5 triggers the low stock alert
INSERT INTO publications (name, type, cost, stock) VALUES
    ('Irish Independent',     'Newspaper', 3, 40),  -- 1
    ('The Irish Times',       'Newspaper', 3, 35),  -- 2
    ('Westmeath Independent', 'Newspaper', 2, 20),  -- 3
    ('Sunday World',          'Newspaper', 3, 4),   -- 4 low stock
    ('RTE Guide',             'Magazine',  4, 12),  -- 5
    ('Farmers Journal',       'Magazine',  5, 2);   -- 6 low stock

-- pattern is Monday..Sunday, 1 = deliver that day
INSERT INTO orders (publicationId, customerId, pattern) VALUES
    (1, 1, '1111110'),  -- 1  Mary: Independent Mon-Sat
    (4, 1, '0000001'),  -- 2  Mary: Sunday World Sun
    (2, 2, '1111111'),  -- 3  John: Irish Times every day
    (3, 3, '0001000'),  -- 4  Siobhan: Westmeath Independent Thu
    (1, 4, '1010100'),  -- 5  Patrick: Independent Mon/Wed/Fri
    (5, 4, '0000010'),  -- 6  Patrick: RTE Guide Sat
    (2, 5, '1111100'),  -- 7  Aoife: Irish Times Mon-Fri
    (6, 6, '0000100'),  -- 8  Liam: Farmers Journal Fri
    (3, 7, '0001000'),  -- 9  Niamh: Westmeath Independent Thu
    (4, 7, '0000001'),  -- 10 Niamh: Sunday World Sun
    (1, 8, '1111110');  -- 11 Sean: Independent Mon-Sat

-- Dockets for every scheduled delivery from the 1st of last month up to yesterday,
-- so "this month" always has data whenever the script is run.
-- About 1 in 9 deliveries is marked as failed so billing can be checked.
-- WEEKDAY() returns 0 for Monday, matching the first character of pattern.
INSERT INTO deliveryDockets (orderId, customerId, date, success)
WITH RECURSIVE days (d) AS (
    SELECT DATE_SUB(DATE_SUB(CURDATE(), INTERVAL DAY(CURDATE()) - 1 DAY), INTERVAL 1 MONTH)
    UNION ALL
    SELECT d + INTERVAL 1 DAY FROM days WHERE d + INTERVAL 1 DAY < CURDATE()
)
SELECT o.orderId, o.customerId, days.d, MOD(o.orderId + DAY(days.d), 9) <> 0
FROM days
JOIN orders o ON SUBSTRING(o.pattern, WEEKDAY(days.d) + 1, 1) = '1'
WHERE days.d < CURDATE()
ORDER BY days.d, o.orderId;

-- passwords are plain text; access_level is 'admin' or 'deliveryPerson'
-- log in by name, e.g. Admin / admin123 or Danny Driver / delivery1
INSERT INTO users (name, password, contact, access_level) VALUES
    ('Admin',         'admin123',    '0870000001', 'admin'),
    ('Danny Driver',  'delivery1',   '0870000002', 'deliveryPerson'),
    ('Sarah Driver',  'delivery2',   '0870000003', 'deliveryPerson');

-- bills are calculated from the dockets in the Bill CLI, so none are inserted here
