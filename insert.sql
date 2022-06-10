-- Basic Insertions so that we can use our Menu

insert into Librarian(Librarian_name, Librarian_pass)
values('admin','pass1234');

insert into Book(book_title, book_author, IssuedBy)
values('book1', 'abc', NULL);

insert into Customer(cust_name)
values('cust1');

insert into Library_Staff(staff_name, staff_pass)
values('staff1','pass1234');
