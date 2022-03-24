package it.polito.ezshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DataBase {
  private EZShop shop;
  private boolean running = false;
  private Integer maxCounterOperations = 0;

  public DataBase(EZShop shop) {
    this.shop = shop; 
  }

  public void setRunning() {
    running = true;
  }
  
  public void destroyEntries() throws Exception {
	  Connection conn = null;
        try {
          conn = DriverManager.getConnection("jdbc:sqlite:entries.db");
          Statement stmt = conn.createStatement();
          stmt.setQueryTimeout(30); // set timeout to 30 sec
          stmt.executeUpdate("drop table if exists Entries");
        } catch (SQLException e) {
          System.err.println(e.getMessage());
        } finally {
          try {
            if (conn != null)
              conn.close();
          } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
          }
        }
  }

  @SuppressWarnings("resource")
  public void createTableRFIDs() throws Exception {
      Connection conn = null;
      boolean flag = false;
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:rfids.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        // stmt.executeUpdate("drop table if exists Customers");
        ResultSet rs = stmt.executeQuery("select * from rfids");
        while (rs.next()) {
          flag = true;
          
          String barCode = rs.getString("barCode");
          String rfid = rs.getString("barCode");
          Integer ticketNumber = rs.getInt("ticketNumber");
  
          if (!shop.recoverRfids(barCode, rfid, ticketNumber))
            throw new Exception("errore caricamento rfids");
        }
      } catch (Exception e) {
        if (!flag) {
          conn = DriverManager.getConnection("jdbc:sqlite:rfids.db");
          Statement stmt = conn.createStatement();
          stmt.setQueryTimeout(30); // set timeout to 30 sec
          stmt.executeUpdate("CREATE TABLE rfids "
              + "(rfid VARCHAR(40) PRIMARY KEY, barCode VARCHAR(40), ticketNumber INTEGER)");
        }
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
        }
      }
    }

    

    public void clearRfids() {
      Connection conn = null;
      if (this.running) {
        try {
          conn = DriverManager.getConnection("jdbc:sqlite:rfids.db");
          Statement stmt = conn.createStatement();
          stmt.setQueryTimeout(30); // set timeout to 30 sec
          stmt.executeUpdate("drop table if exists rfids");
          stmt.executeUpdate("CREATE TABLE rfids "
          + "(rfid VARCHAR(40) PRIMARY KEY, barCode VARCHAR(40), ticketNumber INTEGER))");
        } catch (SQLException e) {
        } finally {
          try {
            if (conn != null)
              conn.close();
          } catch (SQLException e) {
            // connection close failed.
          }
        }
      }
    }

    public boolean clearRfid(String rfid) {
      if (this.running) {
        Connection conn = null;
        String sql = "DELETE FROM rfids WHERE rfid = ?";
        try {
          conn = DriverManager.getConnection("jdbc:sqlite:rfids.db");
          PreparedStatement pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, rfid);      
          pstmt.executeUpdate();
        } catch (SQLException e) {
          return false;
        } finally {
          try {
            if (conn != null)
              conn.close();
          } catch (SQLException e) {
            // connection close failed.
            return false;
          }
        }
      }
    return true;
    }

    public boolean addRfid(String barCode, String rfid, Integer ticketNumber) {
      if (this.running) {
        Connection conn = null;
        String sql = "INSERT INTO rfids VALUES(?,?,?)";
        try {
          conn = DriverManager.getConnection("jdbc:sqlite:rfids.db");
          PreparedStatement pstmt = conn.prepareStatement(sql);
          pstmt.setInt(3, ticketNumber);
          pstmt.setString(2, barCode);
          pstmt.setString(1, rfid);
             
          pstmt.executeUpdate();
        } catch (SQLException e) {
          return false;
        } finally {
          try {
            if (conn != null)
              conn.close();
          } catch (SQLException e) {
            // connection close failed.
            return false;
          }
        }
      }
    return true;
    }


  @SuppressWarnings("resource")
  public void createTableEntries() throws Exception {
      Connection conn = null;
      boolean flag = false;
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:entries.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        // stmt.executeUpdate("drop table if exists Customers");
        ResultSet rs = stmt.executeQuery("select * from Entries");
        while (rs.next()) {
          flag = true;
          
          Integer ticketNumber = rs.getInt("ticketNumber");
          String barCode = rs.getString("barCode");
          String description = rs.getString("description");
          Integer amount = rs.getInt("amount");
          Double pricePerUnit = rs.getDouble("pricePerUnit");
          Double total = rs.getDouble("total");
          Double discountRate = rs.getDouble("discountRate");
          Integer amountReturned = rs.getInt("amountReturned");
  
          if (!shop.recoverTicket(ticketNumber, barCode, description, amount, pricePerUnit,total,discountRate,amountReturned))
            throw new Exception("errore caricamento sale ticket");
        }
      } catch (Exception e) {
        if (!flag) {
          conn = DriverManager.getConnection("jdbc:sqlite:entries.db");
          Statement stmt = conn.createStatement();
          stmt.setQueryTimeout(30); // set timeout to 30 sec
          stmt.executeUpdate("CREATE TABLE Entries "
              + "(ticketNumber INTEGER , barCode VARCHAR(40) , description VARCHAR(40), amount INTEGER, pricePerUnit DOUBLE, discountRate DOUBLE, total DOUBLE,  amountReturned INTEGER, PRIMARY KEY (ticketNumber, barCode))");
        }
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }

    public void clearTableEntries() {
      Connection conn = null;
      if (this.running) {
        try {
          conn = DriverManager.getConnection("jdbc:sqlite:entries.db");
          Statement stmt = conn.createStatement();
          stmt.setQueryTimeout(30); // set timeout to 30 sec
          stmt.executeUpdate("drop table if exists Entries");
          stmt.executeUpdate("CREATE TABLE Entries "
              + "(ticketNumber INTEGER , barCode VARCHAR(40) , description VARCHAR(40), amount INTEGER, pricePerUnit DOUBLE, discountRate DOUBLE, total DOUBLE,  amountReturned INTEGER, PRIMARY KEY (ticketNumber, barCode))");
        } catch (SQLException e) {
          System.err.println(e.getMessage());
        } finally {
          try {
            if (conn != null)
              conn.close();
          } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
          }
        }
      }
    }
    public boolean clearEntry(MyTicketEntry t,Integer ticketNumber) {
      if (this.running) {
        Connection conn = null;
        String sql = "DELETE FROM Entries WHERE ticketNumber = ? AND barCode = ?";
        try {
          conn = DriverManager.getConnection("jdbc:sqlite:entries.db");
          PreparedStatement pstmt = conn.prepareStatement(sql);
          pstmt.setInt(1, ticketNumber);
          pstmt.setString(2, t.getBarCode());      
          pstmt.executeUpdate();
        } catch (SQLException e) {
          return false;
        } finally {
          try {
            if (conn != null)
              conn.close();
          } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
            return false;
          }
        }
      }
    return true;
    }
    public boolean addEntry(MyTicketEntry t,Integer ticketNumber) {
      if (this.running) {
        Connection conn = null;
        String sql = "INSERT INTO Entries VALUES(?,?,?,?,?,?,?,?)";
        try {
          conn = DriverManager.getConnection("jdbc:sqlite:entries.db");
          PreparedStatement pstmt = conn.prepareStatement(sql);
          pstmt.setInt(1, ticketNumber);
          pstmt.setString(2, t.getBarCode());
          pstmt.setString(3, t.getProductDescription());
          pstmt.setInt(4, t.getAmount());
          pstmt.setDouble(5, t.getPricePerUnit());
          pstmt.setDouble(6, t.getDiscountRate());
          pstmt.setDouble(7, t.getTotal());
          pstmt.setInt(8, t.getAmountReturned());         
          pstmt.executeUpdate();
        } catch (SQLException e) {
          System.err.println(e.getMessage());
          return false;
        } finally {
          try {
            if (conn != null)
              conn.close();
          } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
            return false;
          }
        }
      }
    return true;
    }

  @SuppressWarnings("resource")
public void createTableSales() throws Exception {
    Connection conn = null;
    boolean flag = false;
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:sales.db");
      Statement stmt = conn.createStatement();
      stmt.setQueryTimeout(30); // set timeout to 30 sec
      // stmt.executeUpdate("drop table if exists Customers");
      ResultSet rs = stmt.executeQuery("select * from Sales");
      while (rs.next()) {
        flag = true;
        
        Integer ticketNumber = rs.getInt("ticketNumber");
        Double discountRate = rs.getDouble("discountRate");
        Double price = rs.getDouble("price");
        String status = rs.getString("status");
        Integer pointsEarned = rs.getInt("pointsEarned");  

        if (!shop.recoverSale(ticketNumber, discountRate, price, pointsEarned, status))
          throw new Exception("errore caricamento sale operation");
        if (ticketNumber > maxCounterOperations) {
          OperationsCounter.setCounter(ticketNumber + 1);
          maxCounterOperations = ticketNumber;
        }
      }
    } catch (Exception e) {
      if (!flag) {
        conn = DriverManager.getConnection("jdbc:sqlite:sales.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("CREATE TABLE Sales "
            + "(ticketNumber INTEGER PRIMARY KEY, discountRate DOUBLE, price DOUBLE, pointsEarned INTEGER, status VARCHAR(40))");
      }
    } finally {
      try {
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        // connection close failed.
        System.err.println(e.getMessage());
      }
    }
  }

  
  public void destroySales() {
	    Connection conn = null;
	      try {
	        conn = DriverManager.getConnection("jdbc:sqlite:sales.db");
	        Statement stmt = conn.createStatement();
	        stmt.setQueryTimeout(30); // set timeout to 30 sec
	        stmt.executeUpdate("drop table if exists Sales");
	      } catch (SQLException e) {
	        System.err.println(e.getMessage());
	      } finally {
	        try {
	          if (conn != null)
	            conn.close();
	        } catch (SQLException e) {
	          // connection close failed.
	          System.err.println(e.getMessage());
	        }
	      }
  }
  
  public void clearTableSales() {
    Connection conn = null;
    if (this.running) {
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:sales.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("drop table if exists Sales");
        stmt.executeUpdate("CREATE TABLE Sales "
            + "(ticketNumber INTEGER PRIMARY KEY, discountRate DOUBLE, price DOUBLE, pointsEarned INTEGER, status VARCHAR(40))");
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public boolean addSale(MySaleTransaction op) {
    if (this.running) {
      Connection conn = null;
      String sql = "INSERT INTO Sales VALUES(?,?,?,?,?)";
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:sales.db");
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, op.getTicketNumber());
        pstmt.setDouble(2, op.getDiscountRate());
        pstmt.setDouble(3, op.getPrice());
        pstmt.setInt(4, op.getPointsEarned());
        pstmt.setString(5, op.getStatus());
        pstmt.executeUpdate();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
        return false;
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
          return false;
        }
      }
    }
	return true;
  }

  public void createTableOperations() throws Exception {
    Connection conn = null;
    boolean flag = false;
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:operations.db");
      Statement stmt = conn.createStatement();
      stmt.setQueryTimeout(30); // set timeout to 30 sec
      // stmt.executeUpdate("drop table if exists Customers");
      ResultSet rs = stmt.executeQuery("select * from Operations");
      while (rs.next()) {
        flag = true;
        // Integer productID = rs.getInt("productID");
        Double money = rs.getDouble("money");
        Integer balanceID = rs.getInt("balanceID");
        LocalDate date = LocalDate.parse(rs.getString("date"));
        String time = rs.getString("time");
        String typeOp = rs.getString("typeOp");
        String status = rs.getString("status");
        // creare balance operation, aggiornare lista operations in accountBook
        if (!shop.recoverBalanceOperation(balanceID, typeOp, money, date, time, status))
          throw new Exception("errore caricamento balance operation");
        if (balanceID > maxCounterOperations) {
          OperationsCounter.setCounter(balanceID + 1);
          maxCounterOperations = balanceID;
        }
      }
    } catch (Exception e) {
      if (!flag) {
        conn = DriverManager.getConnection("jdbc:sqlite:operations.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("CREATE TABLE Operations "
            + "(balanceID INTEGER PRIMARY KEY, money DOUBLE, date VARCHAR(40), time VARCHAR(40), typeOp VARCHAR(40), status VARCHAR(40))");
      }
    } finally {
      try {
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        // connection close failed.
        System.err.println(e.getMessage());
      }
    }
  }
  
  public void destroyOperations() {
	    Connection conn = null;
	    if (this.running) {
	      try {
	        conn = DriverManager.getConnection("jdbc:sqlite:operations.db");
	        Statement stmt = conn.createStatement();
	        stmt.setQueryTimeout(30); // set timeout to 30 sec
	        stmt.executeUpdate("drop table if exists Operations");
	      } catch (SQLException e) {
	        System.err.println(e.getMessage());
	      } finally {
	        try {
	          if (conn != null)
	            conn.close();
	        } catch (SQLException e) {
	          // connection close failed.
	          System.err.println(e.getMessage());
	        }
	      }
	    }
	  }
  
  public void clearTableOperations() {
    Connection conn = null;
    if (this.running) {
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:operations.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("drop table if exists Operations");
        stmt.executeUpdate("CREATE TABLE Operations "
            + "(balanceID INTEGER PRIMARY KEY, money DOUBLE, date VARCHAR(40), time VARCHAR(40), typeOp VARCHAR(40), status VARCHAR(40))");
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public void addOperation(MyBalanceOperation op) {
    if (this.running) {
      Connection conn = null;
      String sql = "INSERT INTO Operations VALUES(?,?,?,?,?,?)";
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:operations.db");
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, op.getBalanceId());
        pstmt.setDouble(2, op.getMoney());
        pstmt.setString(3, op.getDate().toString());
        pstmt.setString(4, op.getTime());
        pstmt.setString(5, op.getType());
        pstmt.setString(6, op.getStatus());
        pstmt.executeUpdate();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public void createTableOrders() throws Exception {
    Connection conn = null;
    boolean flag = false;
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:orders.db");
      Statement stmt = conn.createStatement();
      stmt.setQueryTimeout(30); // set timeout to 30 sec
      // stmt.executeUpdate("drop table if exists Customers");
      ResultSet rs = stmt.executeQuery("select * from Orders");
      while (rs.next()) {
        flag = true;
        // Integer productID = rs.getInt("productID");
        Integer orderID = rs.getInt("orderID");
        String productCode = rs.getString("productCode");
        Double pricePerUnit = rs.getDouble("pricePerUnit");
        Integer quantity = rs.getInt("quantity");
        String status = rs.getString("status");

        shop.recoverOrder(orderID, productCode, pricePerUnit, quantity, status);

        if (orderID > maxCounterOperations) {
          OperationsCounter.setCounter(orderID + 1);
          maxCounterOperations = orderID;
        }
      }
    } catch (Exception e) {
      if (!flag) {
        conn = DriverManager.getConnection("jdbc:sqlite:orders.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("CREATE TABLE Orders "
            + "(orderID INTEGER PRIMARY KEY, productCode VARCHAR(40), pricePerUnit DOUBLE, quantity INTEGER, status VARCHAR(40))");
      }
    } finally {
      try {
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        // connection close failed.
        System.err.println(e.getMessage());
      }
    }
  }

  public void destroyOrders() {
	    Connection conn = null;
	    if (this.running) {
	      try {
	        conn = DriverManager.getConnection("jdbc:sqlite:orders.db");
	        Statement stmt = conn.createStatement();
	        stmt.setQueryTimeout(30); // set timeout to 30 sec
	        stmt.executeUpdate("drop table if exists Orders");
	      } catch (SQLException e) {
	        System.err.println(e.getMessage());
	      } finally {
	        try {
	          if (conn != null)
	            conn.close();
	        } catch (SQLException e) {
	          // connection close failed.
	          System.err.println(e.getMessage());
	        }
	      }
	    }
  }
	    
  public void clearTableOrders() {
    Connection conn = null;
    if (this.running) {
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:orders.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("drop table if exists Orders");
        stmt.executeUpdate("CREATE TABLE Orders "
            + "(orderID INTEGER PRIMARY KEY, productCode VARCHAR(40), pricePerUnit DOUBLE, quantity INTEGER, status VARCHAR(40))");
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public void addOrder(MyOrder op) {
    if (this.running) {
      Connection conn = null;
      String sql = "INSERT INTO Orders VALUES(?,?,?,?,?)";
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:orders.db");
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, op.getOrderId());
        pstmt.setString(2, op.getProductCode());
        pstmt.setDouble(3, op.getPricePerUnit());
        pstmt.setInt(4, op.getQuantity());
        pstmt.setString(5, op.getStatus());
        pstmt.executeUpdate();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public void createTableProducts() throws Exception {
    Connection conn = null;
    boolean flag = false;
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:products.db");
      Statement stmt = conn.createStatement();
      stmt.setQueryTimeout(30); // set timeout to 30 sec
      // stmt.executeUpdate("drop table if exists Customers");
      ResultSet rs = stmt.executeQuery("select * from Products");
      while (rs.next()) {
        flag = true;
        // Integer productID = rs.getInt("productID");
        String barCode = rs.getString("barCode");
        String description = rs.getString("description");
        Double sellPrice = rs.getDouble("sellPrice");
        String notes = rs.getString("notes");
        Integer quantity = rs.getInt("quantity");
        // Integer discountRate = rs.getInt("discountRate");
        String position = rs.getString("position");
        shop.createUser("SuperUser", "1234", "Administrator");
        shop.login("SuperUser", "1234");
        Integer new_pID = shop.createProductType(description, barCode, sellPrice, notes);
        shop.updatePosition(new_pID, position);
        shop.updateQuantity(new_pID, quantity);
      }
    } catch (Exception e) {
      if (!flag) {
        conn = DriverManager.getConnection("jdbc:sqlite:products.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("CREATE TABLE Products "
            + "(productID INTEGER PRIMARY KEY, barCode VARCHAR(40), description VARCHAR(40), sellPrice DOUBLE, notes VARCHAR(40), quantity INTEGER, position VARCHAR(40))");
      }
    } finally {
      try {
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        // connection close failed.
        System.err.println(e.getMessage());
      }
    }
  }
  
  public void destroyProducts() {
	    Connection conn = null;
	    if (this.running) {
	      try {
	        conn = DriverManager.getConnection("jdbc:sqlite:products.db");
	        Statement stmt = conn.createStatement();
	        stmt.setQueryTimeout(30); // set timeout to 30 sec
	        stmt.executeUpdate("drop table if exists Products");
	      } catch (SQLException e) {
	        System.err.println(e.getMessage());
	      } finally {
	        try {
	          if (conn != null)
	            conn.close();
	        } catch (SQLException e) {
	          // connection close failed.
	          System.err.println(e.getMessage());
	        }
	      }
	    }
	  }
  
  public void clearTableProducts() {
    Connection conn = null;
    if (this.running) {
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:products.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("drop table if exists Products");
        stmt.executeUpdate("CREATE TABLE Products "
            + "(productID INTEGER PRIMARY KEY, barCode VARCHAR(40), description VARCHAR(40), sellPrice DOUBLE, notes VARCHAR(40), quantity INTEGER, position VARCHAR(40))");
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public void addProduct(Integer productID, String barCode, String description, Double sellPrice, String notes,
      Integer quantity, String position) {
    if (this.running) {
      Connection conn = null;
      String sql = "INSERT INTO Products(productID,barCode,description,sellPrice,notes,quantity,position) VALUES(?,?,?,?,?,?,?)";
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:products.db");
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, productID);
        pstmt.setString(2, barCode);
        pstmt.setString(3, description);
        pstmt.setDouble(4, sellPrice);
        pstmt.setString(5, notes);
        pstmt.setInt(6, quantity);
        pstmt.setString(7, position);
        pstmt.executeUpdate();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public void createTableEmployees() throws Exception {
    Connection conn = null;
    boolean flag = false;
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:employees.db");
      Statement stmt = conn.createStatement();
      stmt.setQueryTimeout(30); // set timeout to 30 sec
      // stmt.executeUpdate("drop table if exists Customers");
      ResultSet rs = stmt.executeQuery("select * from Employees");
      while (rs.next()) {
        flag = true;
        Integer userID = rs.getInt("userID");
        String role = rs.getString("role");
        String username = rs.getString("username");
        String password = rs.getString("password");
        shop.createUser("SuperUser", "1234", "Administrator");
        shop.login("SuperUser", "1234");
        shop.createUser(username, password, role);
      }
    } catch (Exception e) {
      if (!flag) {
        conn = DriverManager.getConnection("jdbc:sqlite:employees.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("CREATE TABLE Employees "
            + "(userID INTEGER PRIMARY KEY, role VARCHAR(40), username VARCHAR(40), password VARCHAR(40))");
      }
    } finally {
      try {
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        // connection close failed.
        System.err.println(e.getMessage());
      }
    }
  }
  
  public void destroyEmployees() {
	    Connection conn = null;
	    if (this.running) {
	      try {
	        conn = DriverManager.getConnection("jdbc:sqlite:employees.db");
	        Statement stmt = conn.createStatement();
	        stmt.setQueryTimeout(30); // set timeout to 30 sec
	        stmt.executeUpdate("drop table if exists Employees");
	      } catch (SQLException e) {
	        System.err.println(e.getMessage());
	      } finally {
	        try {
	          if (conn != null)
	            conn.close();
	        } catch (SQLException e) {
	          // connection close failed.
	          System.err.println(e.getMessage());
	        }
	      }
	    }
	  }

  public void clearTableEmployees() {
    Connection conn = null;
    if (this.running) {
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:employees.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("drop table if exists Employees");
        stmt.executeUpdate("CREATE TABLE Employees "
            + "(userID INTEGER PRIMARY KEY, role VARCHAR(40), username VARCHAR(40), password VARCHAR(40))");
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public void addUser(Integer userID, String name, String password, String role) {
    if (this.running) {
      Connection conn = null;
      String sql = "INSERT INTO Employees(userID,role,username,password) VALUES(?,?,?,?)";
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:employees.db");
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userID);
        pstmt.setString(3, name);
        pstmt.setString(4, password);
        pstmt.setString(2, role);
        pstmt.executeUpdate();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public void createTableCustomers() throws Exception {
    Connection conn = null;
    boolean flag = false;
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:customers.db");
      Statement stmt = conn.createStatement();
      stmt.setQueryTimeout(30); // set timeout to 30 sec
      // stmt.executeUpdate("drop table if exists Customers");
      ResultSet rs = stmt.executeQuery("select * from Customers");
      while (rs.next()) {
        flag = true;
        Integer customerID = rs.getInt("customerID");
        String name = rs.getString("name");
        String lcardID = rs.getString("lcardID");
        Integer lcardPoints = rs.getInt("lcardPoints");
        shop.createUser("SuperUser", "1234", "Administrator");
        shop.login("SuperUser", "1234");
        shop.defineCustomer(name);
        if (lcardID.compareTo("null") != 0 && lcardID.compareTo("") != 0) {
          shop.attachCardToCustomer(lcardID, customerID);
          if (lcardPoints > 0)
            shop.modifyPointsOnCard(lcardID, lcardPoints);
          Integer newCounter = Integer.parseInt(lcardID) + 1;
          CardCounter.setCounter(newCounter.toString());
        }
      }
    } catch (Exception e) {
      if (!flag) {
        conn = DriverManager.getConnection("jdbc:sqlite:customers.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("CREATE TABLE Customers "
            + "(customerID INTEGER PRIMARY KEY, name VARCHAR(40), lcardID VARCHAR(40), lcardPoints INTEGER)");
      }
    } finally {
      try {
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        // connection close failed.
        System.err.println(e.getMessage());
      }
    }
  }

  
  public void destroyCustomers() {
	    Connection conn = null;
	    if (this.running) {
	      try {
	        conn = DriverManager.getConnection("jdbc:sqlite:customers.db");
	        Statement stmt = conn.createStatement();
	        stmt.setQueryTimeout(30); // set timeout to 30 sec
	        stmt.executeUpdate("drop table if exists Customers");
	      } catch (SQLException e) {
	        System.err.println(e.getMessage());
	      } finally {
	        try {
	          if (conn != null)
	            conn.close();
	        } catch (SQLException e) {
	          // connection close failed.
	          System.err.println(e.getMessage());
	        }
	      }
	    }
	  }
  
  public void clearTableCustomers() {
    Connection conn = null;
    if (this.running) {
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:customers.db");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // set timeout to 30 sec
        stmt.executeUpdate("drop table if exists Customers");
        stmt.executeUpdate("CREATE TABLE Customers "
            + "(customerID INTEGER PRIMARY KEY, name VARCHAR(40), lcardID VARCHAR(40), lcardPoints INTEGER)");
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public void addCustomer(Integer customerID, String name, String lcardID, Integer lcardPoints) {
    if (this.running) {
      Connection conn = null;
      String sql = "INSERT INTO Customers(customerID,name,lcardID,lcardPoints) VALUES(?,?,?,?)";
      if (lcardID == null)
        lcardID = "null";
      if (lcardPoints == null)
        lcardPoints = 0;
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:customers.db");
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerID);
        pstmt.setString(2, name);
        pstmt.setString(3, lcardID);
        pstmt.setInt(4, lcardPoints);
        pstmt.executeUpdate();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          if (conn != null)
            conn.close();
        } catch (SQLException e) {
          // connection close failed.
          System.err.println(e.getMessage());
        }
      }
    }
  }

  /*
   * public void addUser(Integer userID, String role, String name, String
   * password) { Connection conn = null; try{ conn =
   * DriverManager.getConnection("jdbc:sqlite:customers.db"); Statement stmt =
   * conn.createStatement(); stmt.setQueryTimeout(30); // set timeout to 30 sec
   * String s = new
   * String("insert into Employees(customerID, name, lcardID, lcardPoints) " +
   * "values(" + Integer.toString(customerID) + ", '" + name + "', '" + lcardID +
   * "', " + Integer.toString(lcardPoints) + ")"); stmt.executeUpdate(s);
   * }catch(SQLException e) { System.err.println(e.getMessage()); } finally { try
   * { if(conn != null) conn.close(); } catch(SQLException e) { // connection
   * close failed. System.err.println(e.getMessage()); } } }
   * 
   * 
   * 
   * /*public static void main(String[] args) { Connection connection = null; try
   * { // create a database connection connection =
   * DriverManager.getConnection("jdbc:sqlite:sample.db"); Statement statement =
   * connection.createStatement(); statement.setQueryTimeout(30); // set timeout
   * to 30 sec.
   * 
   * statement.executeUpdate("drop table if exists person");
   * statement.executeUpdate("create table person (id integer, name string)");
   * statement.executeUpdate("insert into person values(1, 'leo')");
   * statement.executeUpdate("insert into person values(2, 'yui')"); ResultSet rs
   * = statement.executeQuery("select * from person"); while(rs.next()) { // read
   * the result set System.out.println("name = " + rs.getString("name"));
   * System.out.println("id = " + rs.getInt("id")); } } catch(SQLException e) { //
   * if the error message is "out of memory", // it probably means no database
   * file is found System.err.println(e.getMessage()); } finally { try {
   * if(connection != null) connection.close(); } catch(SQLException e) { //
   * connection close failed. System.err.println(e.getMessage()); } } }
   */
}