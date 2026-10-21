CREATE DATABASE newsPaperSystemDatabase;

CREATE TABLE newsPaperSystemDatabase.customer (
    customerId INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    address    VARCHAR(150) NOT NULL,
    contact    VARCHAR(150) NOT NULL
);

CREATE TABLE newsPaperSystemDatabase.publications (
    publicationsId INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(150) NOT NULL,
    type  VARCHAR(150) NOT NULL,
    cost  INT          NOT NULL,
    stock INT          NOT NULL
);

CREATE TABLE newsPaperSystemDatabase.orders (
    ordersId       INT AUTO_INCREMENT PRIMARY KEY,
    publicationId INT          NOT NULL,
    customerId    INT          NOT NULL,
    pattern        VARCHAR(150) NOT NULL,
    FOREIGN KEY (publicationId) REFERENCES publications(publicationsId),
    FOREIGN KEY (customerId)    REFERENCES customer(customerId)
);

CREATE TABLE newsPaperSystemDatabase.deliveryDocket (
    deliveryDocketId INT AUTO_INCREMENT PRIMARY KEY,
    orderId         INT NOT NULL,
    customerId      INT NOT NULL,
    FOREIGN KEY (orderId)    REFERENCES orders(ordersId),
    FOREIGN KEY (customerId) REFERENCES customer(customerId)
);

CREATE TABLE newsPaperSystemDatabase.deliveryArea (
    deliveryAreaId INT AUTO_INCREMENT PRIMARY KEY,
    location       VARCHAR(150) NOT NULL
);

CREATE TABLE newsPaperSystemDatabase.user (
    userId       INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(150) NOT NULL,
    password     VARCHAR(150) NOT NULL,
    contact      VARCHAR(150) NOT NULL,
    access_level VARCHAR(150) NOT NULL
);

CREATE TABLE newsPaperSystemDatabase.bill (
    billId      INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT NOT NULL,
    orderId    INT NOT NULL,
    FOREIGN KEY (customerId) REFERENCES customer(customerId),
    FOREIGN KEY (orderId)    REFERENCES orders(ordersId)
);