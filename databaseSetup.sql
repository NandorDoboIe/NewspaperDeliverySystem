CREATE DATABASE newsPaperSystemDatabase;

CREATE TABLE newsPaperSystemDatabase.customer(
customerId INT AUTO_INCREMENT PRIMARY KEY,
firstName VARCHAR(50) NOT NULL,
surname VARCHAR(50) NOT NULL,
address VARCHAR(150) NOT NULL,
phoneNumber VARCHAR(20) NOT NULL
);
