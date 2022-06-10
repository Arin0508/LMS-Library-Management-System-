/* Adding foreign key constraint to the book table */
alter table Book
add constraint fk_book FOREIGN KEY(IssuedBy) references Customer(cust_id);