CREATE DATABASE newsPaperSystemDatabase;

CREATE TABLE newsPaperSystemDatabase.customers (
    customerId INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    address    VARCHAR(150) NOT NULL,
    contact    VARCHAR(150) NOT NULL
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
    orderId         INT NOT NULL,
    customerId      INT NOT NULL,
    FOREIGN KEY (orderId)    REFERENCES orders(orderId),
    FOREIGN KEY (customerId) REFERENCES customers(customerId)
);

CREATE TABLE newsPaperSystemDatabase.deliveryAreas (
    deliveryAreaId INT AUTO_INCREMENT PRIMARY KEY,
    location       VARCHAR(150) NOT NULL
);

CREATE TABLE newsPaperSystemDatabase.users (
    userId       INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(150) NOT NULL,
    password     VARCHAR(150) NOT NULL,
    contact      VARCHAR(150) NOT NULL,
    access_level VARCHAR(150) NOT NULL
);

CREATE TABLE newsPaperSystemDatabase.bills (
    billId      INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT NOT NULL,
    orderId    INT NOT NULL,
    FOREIGN KEY (customerId) REFERENCES customers(customerId),
    FOREIGN KEY (orderId)    REFERENCES orders(orderId)
);