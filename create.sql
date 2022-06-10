/*Creating Database*/
create database LMS;

use LMS;

-- Creating Tables
CREATE TABLE Book 
(
	book_id INT NOT NULL AUTO_INCREMENT,
	book_title VARCHAR(255) NOT NULL,
	book_author VARCHAR(255),
	IssuedBy INT ,
	constraint pk_book PRIMARY KEY (book_id)
);

CREATE TABLE Customer 
(
	cust_id INT NOT NULL AUTO_INCREMENT,
	cust_name VARCHAR(255) NOT NULL,
	constraint pk_student PRIMARY KEY (cust_id)
);

CREATE TABLE Library_Staff 
(
	staff_id INT NOT NULL AUTO_INCREMENT,
	staff_name VARCHAR(255) NOT NULL,
	staff_pass VARCHAR(255) NOT NULL,
	constraint pk_satff PRIMARY KEY (staff_id)
);

CREATE TABLE Librarian 
(
	Librarian_id INT NOT NULL AUTO_INCREMENT,
	Librarian_name VARCHAR(255) NOT NULL,
	Librarian_pass VARCHAR(255) NOT NULL,
	constraint pk_Librarian PRIMARY KEY (Librarian_id)
);
