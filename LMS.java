//STEP 1. Import required packages

import java.sql.*;
import java.util.Scanner;

public class LMS 
{
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/LMS?allowPublicKeyRetrieval=true&useSSL=false";
   static final String USER = "root";
   static final String PASS = "admin";

   public static void main(String[] args) 
   {

      Connection conn = null;
      Statement stmt = null;
      try 
      {
         Class.forName(JDBC_DRIVER);
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();
         Scanner scan = new Scanner(System.in); // Create a Scanner object

         flushScreen(); //Function to clear the whole screen
         System.out.println("\nWELCOME TO LIBRARY MANAGEMENT SERVICE\n");
         menu(stmt, scan);
         scan.close();
         stmt.close();
         conn.close();
      } 
      catch (SQLException se) 
      { 
         // se.printStackTrace();
      } 
      catch (Exception e) 
      { 
         // e.printStackTrace();
      } 
      finally 
      {
         try {
            if (stmt != null)
               stmt.close();
         } catch (SQLException se2) {
         }
         try {
            if (conn != null)
               conn.close();
         } catch (SQLException se) {
            // se.printStackTrace();
         }
      }
   }

static void menu(Statement stmt, Scanner scan) 
{
      System.out.println("Login as a -");
      System.out.println("1. Customer");
      System.out.println("2. Staff Member");
      System.out.println("3. Librarian");
      System.out.println("0. Exit");
      System.out.print("\n\nYour Choice : ");

      int option = Integer.parseInt(scan.nextLine());
      flushScreen();

      switch(option) 
      {
         case 0:
            System.out.println("\n\nThank you!! We were happy to serve you, Come Again !!\n\n");
            System.exit(0);
         case 1:
            customer_menu(stmt, scan);
            break;
         case 2:
            authenticate_staff(stmt, scan);
            break;
         case 3:
            authenticate_librarian(stmt, scan);
            break;
         default:
            flushScreen();
            System.out.println("Choose an appropriate option!!\n");
            break;
      }
      menu(stmt, scan);
   }

   //A common Authentication FUnction for both staff and Librarian
   static boolean authentication(Statement stmt, Scanner scan, boolean isAdmin) 
   {
      System.out.print("Enter your ID : ");
      String id = scan.nextLine();
      System.out.print("Enter your password : ");
      String password = scan.nextLine();
      flushScreen();
      boolean checkAuth = false;

      if(isAdmin) 
      {
         String sql = "select * from Librarian;";
         ResultSet rs = executeSqlStmt(stmt, sql);

         try 
         {
            while(rs.next()) 
            {
               String readid = rs.getString("Librarian_id");
               String readpassword = rs.getString("Librarian_pass");

               if(readid.equals(id) && readpassword.equals(password)) //String object has method of equals
               {
                  checkAuth = true;
                  break;
               }
            }
         } 
         catch (SQLException se) 
         {
         }
      } 
      else
      {
         String sql = "select * from Library_Staff ;";
         ResultSet rs = executeSqlStmt(stmt, sql);

         try 
         {
            while(rs.next()) 
            {
               String readid = rs.getString("staff_id");
               String readpassword = rs.getString("staff_pass");

               if(readid.equals(id) && readpassword.equals(password)) 
               {
                  checkAuth = true;
                  break;
               }
            }
         } 
         catch (SQLException se) 
         {
         }
      }

      return checkAuth;
   }

   static void authenticate_staff(Statement stmt, Scanner scan) 
   {
      if(authentication(stmt, scan, false)) 
      {
         staff_menu(stmt, scan);
      } 
      //If a user fails to login successfully, then we will ask him to enter his credentials again
      else 
      {
         System.out.print("Invalid Username or Password. Do you want to try again (Y/N)? ");
         String input = scan.nextLine();

         if(input.equals("Y") || input.equals("Y"))
            authenticate_staff(stmt, scan);
         else
            return;
      }
   }

   static void authenticate_librarian(Statement stmt, Scanner scan) 
   {
      if(authentication(stmt, scan, true)) 
      {
         librarian_menu(stmt, scan);
      } 
      else 
      {
         System.out.print("Invalid Username or Password. Do you want to try again (Y/N)? ");
         String input = scan.nextLine();

         if(input.equals("Y") || input.equals("y"))
            authenticate_librarian(stmt, scan);
         else
            return;
      }
   }

   static void customer_menu(Statement stmt, Scanner scan) 
   {
      System.out.println("Services we can provide -");
      System.out.println("1. List of available Books");
      System.out.println("0. Exit");

      System.out.print("\n\nYour Choice : ");
      int option = Integer.parseInt(scan.nextLine());
      flushScreen();

      switch(option) 
      {
         case 0:
            return;
         case 1:
            listofBooks(stmt, scan, true);
            break;
         default:
            flushScreen();
            System.out.println("Please select an appropriate option!!\n");
            break;
      }
      customer_menu(stmt, scan);
   }

   static void staff_menu(Statement stmt, Scanner scan) 
   {
      System.out.println("Please select an appropriate option-");
      System.out.println("1. List of all books");
      System.out.println("2. List of available books");
      System.out.println("3. Issue a book");
      System.out.println("4. Return a book");
      System.out.println("5. Add a book");
      System.out.println("6. Delete a book");
      System.out.println("0. Exit");
      System.out.print("\n\nYour choice : ");

      int option = Integer.parseInt(scan.nextLine());
      flushScreen();
      switch(option) 
      {
         case 0:
            return;
         case 1:
            listofBooks(stmt, scan, false);
            break;
         case 2:
            listofBooks(stmt, scan, true);
            break;
         case 3:
            issueBook(stmt, scan);
            break;
         case 4:
            returnBook(stmt, scan);
            break;
         case 5:
            addBook(stmt, scan);
            break;
         case 6:
            deleteBook(stmt, scan);
            break;
         default:
            flushScreen();
            System.out.println("Please select an appropriate option!!\n");
            break;
      }
      staff_menu(stmt, scan);
   }

   static void librarian_menu(Statement stmt, Scanner scan) 
   {
      System.out.println("Please select an appropriate option-");
      System.out.println("1. List of Customers");
      System.out.println("2. List of Staff Members");
      System.out.println("3. Add a Customer");
      System.out.println("4. Delete a Customer");
      System.out.println("5. Add a Staff Member");
      System.out.println("6. Delete a Staff Member");
      System.out.println("0. Exit");
      System.out.print("\n\nYour Choice: ");

      int option = Integer.parseInt(scan.nextLine());
      flushScreen();

      switch(option) 
      {
         case 0:
            return;
         case 1:
            listofCustomers(stmt, scan);
            break;
         case 2:
            listofStaff(stmt, scan);
            break;
         case 3:
            addCust(stmt, scan);
            break;
         case 4:
            deleteCust(stmt, scan);
            break;
         case 5:
            addStaff(stmt, scan);
            break;
         case 6:
            deleteStaff(stmt, scan);
            break;
         default:
            flushScreen();
            System.out.println("Please select an appropriate option!!\n");
            break;
      }
      librarian_menu(stmt, scan);
   }

   //A common Function to print list of Available Books and list of all Books
   static boolean listofBooks(Statement stmt, Scanner scan, boolean check) 
   {
      String sql = "select * from Book;";
      ResultSet rs = executeSqlStmt(stmt, sql);
      boolean checkBook = true; //Variable which will be true when there are no Books available

      try 
      {
         if(check)   System.out.println("List of Available Books :\n");
         else  System.out.println("List of Books :\n");
         while(rs.next()) 
         {
            String id = rs.getString("book_id");
            String title = rs.getString("book_title");
            String author = rs.getString("book_author");
            String IssuedBy = rs.getString("IssuedBy");

            if(check) 
            {
               if(IssuedBy == null) 
               {
                  System.out.println("ID : " + id);
                  System.out.println("Book title: " + title);
                  System.out.println("Author : " + author);
                  System.out.println("");
                  checkBook = false;
               }
            } 
            else 
            {
               System.out.println("ID : " + id);
               System.out.println("Book title: " + title);
               System.out.println("Author : " + author);
               System.out.println("IssuedBy : " + IssuedBy);
               System.out.println("");
               checkBook = false;
            }
         }

         if(checkBook)  System.out.println("No Books are available !! Come Again After sometime ...\n");
         
         rs.close();
      } 
      catch (SQLException e) 
      {
         // e.printStackTrace();
      }
      return checkBook;
   }

   static boolean checkforIssue(Statement stmt, Scanner scan, int id)
   {
      String sql = String.format("select IssuedBy from Book where book_id = %d;", id);
      ResultSet rs = executeSqlStmt(stmt, sql);
      boolean isAvailable = false;

      try 
      {
         while(rs.next()) 
         {
            String IssuedBy = rs.getString("IssuedBy");
            isAvailable = true;
            if(IssuedBy != null) 
            { 
               isAvailable = false;
            } 
         }
         rs.close();
      } 
      catch (SQLException e) 
      {
         // e.printStackTrace();
      }
      return isAvailable;
   }
   
   static void issueBook(Statement stmt, Scanner scan) 
   {
      try 
      {
         //Checking whether there are any books or not
         boolean noBooks = listofBooks(stmt, scan, true); //Printing available Books
         if (!noBooks) 
         {
            System.out.println("Select a Book from the above list...");
            System.out.print("\nEnter Book ID : ");
            int id = Integer.parseInt(scan.nextLine());

            System.out.print("Enter Customer ID : ");
            int cust_id = Integer.parseInt(scan.nextLine());

            if(checkforIssue(stmt, scan, id) == false)
            {
               System.out.println("Book is not available !!\nDo you want to issue another available book (Y/N)?");
               String input = scan.nextLine();
               if(input.equals("Y") || input.equals("y"))
               {   
                  flushScreen();
                  issueBook(stmt, scan);
               }
               else
               {   
                  flushScreen();
                  return;
               }
            }
            flushScreen();

            String sql = String.format("update Book set IssuedBy = %d where book_id = %d;", cust_id, id);
            int result = updateSqlStmt(stmt, sql);

            if (result != 0)  System.out.println("Book Issued Successfully!!\n");
            else  System.out.println("Something went wrong!!\n");
         }
      } 
      catch (Exception e) 
      {
         // e.printStackTrace();
      }
   }

   static void returnBook(Statement stmt, Scanner scan) 
   {
      try 
      {
         System.out.print("\nEnter Book ID : ");
         int id = Integer.parseInt(scan.nextLine());

         flushScreen();

         String sql = String.format("update Book set IssuedBy = NULL where book_id = %d ;", id);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)
            System.out.println("Book returned Successfully!!\n");
         else
            System.out.println("Something went wrong!!\n");
      } 
      catch (Exception e) 
      {
         // e.printStackTrace();
      }
   }

   static void addBook(Statement stmt, Scanner scan) 
   {
      try 
      {
         System.out.print("\nEnter Book Title : ");
         String title = scan.nextLine();
         // System.out.println(title);
         System.out.print("\nEnter Book Author : ");
         String author = scan.nextLine();

         flushScreen();

         String sql = String.format("insert into Book(book_title, book_author) values('%s', '%s');", title, author);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)  System.out.println("Book added successfully!!\n");
         else  System.out.println("Something went wrong!!\n");
      } 
      catch (Exception e) 
      {
         // e.printStackTrace();
      }
   }

   static void deleteBook(Statement stmt, Scanner scan) 
   {
      try {
         System.out.print("\nEnter Book ID : ");
         int id = Integer.parseInt(scan.nextLine());

         flushScreen();

         String sql = String.format("delete from Book where book_id = %d ;", id);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)  System.out.println("Book deleted successfully!!\n");
         else  System.out.println("Something went wrong!\n");
      } 
      catch (Exception e) 
      {
         // e.printStackTrace();
      }
   }

   static void listofCustomers(Statement stmt, Scanner scan) 
   {
      String sql = "select * from Customer;";
      ResultSet rs = executeSqlStmt(stmt, sql);

      try 
      {
         System.out.println("List of Customers :\n");
         while (rs.next()) 
         {
            String id = rs.getString("cust_id");
            String name = rs.getString("cust_name");

            System.out.println("Cust ID : " + id);
            System.out.println("Name: " + name);
            System.out.println("\n");
         }

         rs.close();
      } 
      catch (SQLException e) 
      {
         // e.printStackTrace();
      }
   }

   static void listofStaff(Statement stmt, Scanner scan) 
   {
      String sql = "select * from Library_Staff";
      ResultSet rs = executeSqlStmt(stmt, sql);

      try 
      {
         System.out.println("List of Staff Members :\n");
         while (rs.next()) {
            String id = rs.getString("staff_id");
            String name = rs.getString("staff_name");

            System.out.println("ID : " + id);
            System.out.println("Name: " + name);
            System.out.println("\n");
         }

         rs.close();
      } 
      catch (SQLException e) 
      {
         // e.printStackTrace();
      }
   }

   //To get the curr id number from staff and customer table
   static void currID(Statement stmt, Scanner scan, String Table)
   {
      String id_name;

      if(Table.equals("Customer"))
      {
         id_name = "cust_id";
      }
      else
      {
         id_name = "staff_id";
      }

      String sql = "select max(" + id_name + ") as maxy from " + Table;
      ResultSet rs = executeSqlStmt(stmt, sql);
      try 
      {
         while(rs.next()) 
         {
            String id = rs.getString("maxy");  
            System.out.println(Table + " ID Created : " + (id));
            System.out.println("\n");
         }

         rs.close();
      } 
      catch (SQLException e) 
      {
            // e.printStackTrace();
      }
   }

   static void addCust(Statement stmt, Scanner scan) 
   {
      try 
      {
         System.out.print("Enter Customer name : ");
         String name = scan.nextLine();

         flushScreen();

         String sql = String.format("insert into Customer(cust_name) values('%s');", name);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)  
         {   
            System.out.println("Customer added Successfully!!\n");
            currID(stmt, scan, "Customer");
         }
         else
            System.out.println("Something went wrong!!\n");
      } 
      catch(Exception e) 
      {
         // e.printStackTrace();
      }
   }

   static void addStaff(Statement stmt, Scanner scan) 
   {
      try 
      {
         System.out.print("Enter name : ");
         String name = scan.nextLine();
         System.out.print("Enter password : ");
         String password = scan.nextLine();

         flushScreen();

         String sql = String.format("insert into Library_Staff(staff_name, staff_pass) values('%s', '%s');", name, password);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)  
         {   
            System.out.println("Staff Member added Successfully!!\n");
            currID(stmt, scan, "Library_Staff");
         }
         else
            System.out.println("Something went wrong!!\n");
      } 
      catch (Exception e) 
      {
         // e.printStackTrace();
      }
   }

   static void deleteCust(Statement stmt, Scanner scan) 
   {
      try 
      {
         System.out.print("Enter Customer ID : ");
         String id = scan.nextLine();

         flushScreen();

         String sql = String.format("delete from Customer where cust_id = '%s';", id);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)  System.out.println("Customer deleted successfully!!\n");
         else
            System.out.println("Something went wrong!!\n");
      } 
      catch (Exception e) 
      {
         // e.printStackTrace();
      }
   }

   static void deleteStaff(Statement stmt, Scanner scan) 
   {
      try 
      {
         System.out.print("Enter staff Member's ID : ");
         String id = scan.nextLine();

         flushScreen();

         String sql = String.format("delete from Library_Staff where staff_id = '%s';", id);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)  System.out.println("Staff Member deleted successfully!!\n");
         else  System.out.println("Something went wrong!!\n");
      } 
      catch(Exception e) 
      {
         // e.printStackTrace();
      }
   }

   static ResultSet executeSqlStmt(Statement stmt, String sql) {
      try {
         ResultSet rs = stmt.executeQuery(sql);
         return rs;
      } catch (SQLException se) {
         // se.printStackTrace();
      } catch (Exception e) {
         // e.printStackTrace();
      }
      return null;
   }

   static int updateSqlStmt(Statement stmt, String sql) {
      try 
      {
         int rs = stmt.executeUpdate(sql);
         return rs;
      } 
      catch (SQLException se) 
      {
         // se.printStackTrace();
      } catch (Exception e) 
      {
         // e.printStackTrace();
      }
      return 0;
   }

   static void flushScreen() 
   {
      System.out.println("\033[H\033[J"); //Using escape code we will clear out the whole screen from the cursor position to the bottom of screen
      System.out.flush();
   }         
               
               //end main
}					//end class


//Note : By default autocommit is on. you can set to false using con.setAutoCommit(false)
