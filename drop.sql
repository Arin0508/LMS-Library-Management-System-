-- Droping Foreign key constraint
alter table Book
drop foreign key fk_book;

-- Droping Tables
drop table Book;
drop table Customer;
drop table Library_Staff;
drop table Librarian;

-- Droping whole database
drop database LMS;