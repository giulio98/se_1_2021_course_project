package integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

import it.polito.ezshop.data.DataBase;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.MyBalanceOperation;
import it.polito.ezshop.data.MyOrder;
import it.polito.ezshop.data.MySaleTransaction;
import it.polito.ezshop.data.MyTicketEntry;

public class TestDatabase {

	@Test
	public void testCreateTableEntries() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.destroyEntries();
        assertThrows(SQLiteException.class,()->{
        	Connection conn = null;
    		conn = DriverManager.getConnection("jdbc:sqlite:entries.db");
    		Statement stmt = conn.createStatement();
    		stmt.setQueryTimeout(30);
    		ResultSet rs = stmt.executeQuery("select * from Entries");});
        db.createTableEntries();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:entries.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Entries");
		assertFalse(rs.next());
		conn.close();
	}
	
	@Test
	public void testAddEntry() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.clearTableEntries();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:entries.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Entries");
		assertFalse(rs.next());
		MyTicketEntry t = new MyTicketEntry("123",1,1.0,"ciao");
		db.addEntry(t,2);
		rs = stmt.executeQuery("select * from Entries");
		assertTrue(rs.next());
		db.createTableEntries(); 
		conn.close();
	}
	
	@Test
	public void testCreateTableSales() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.destroySales();
        assertThrows(SQLiteException.class,()->{
        	Connection conn = null;
    		conn = DriverManager.getConnection("jdbc:sqlite:sales.db");
    		Statement stmt = conn.createStatement();
    		stmt.setQueryTimeout(30);
    		ResultSet rs = stmt.executeQuery("select * from Sales");});
        db.createTableSales();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:sales.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Sales");
		assertFalse(rs.next());
		conn.close();
	}
	
	@Test
	public void testAddSale() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.destroySales();
		db.clearTableSales();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:sales.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Sales");
		assertFalse(rs.next());
		MySaleTransaction op = new MySaleTransaction(1);
		db.addSale(op);
		rs = stmt.executeQuery("select * from Sales");
		assertTrue(rs.next());
		db.createTableSales();
		conn.close();
	}
	
	@Test
	public void testCreateTableOperations() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.destroyOperations();
        assertThrows(SQLiteException.class,()->{
        	Connection conn = null;
    		conn = DriverManager.getConnection("jdbc:sqlite:operations.db");
    		Statement stmt = conn.createStatement();
    		stmt.setQueryTimeout(30);
    		ResultSet rs = stmt.executeQuery("select * from Operations");});
        db.createTableOperations();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:operations.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Operations");
		assertFalse(rs.next());
		conn.close();
	}
	
	@Test
	public void testAddOperation() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.clearTableOperations();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:operations.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Operations");
		assertFalse(rs.next());
		MyBalanceOperation op = new MyBalanceOperation(1,"ciao");
		db.addOperation(op);
		rs = stmt.executeQuery("select * from Operations");
		assertTrue(rs.next());
		db.createTableOperations();
		conn.close();
	}
	
	@Test
	public void testCreateTableOrders() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.destroyOrders();
        assertThrows(SQLiteException.class,()->{
        	Connection conn = null;
    		conn = DriverManager.getConnection("jdbc:sqlite:orders.db");
    		Statement stmt = conn.createStatement();
    		stmt.setQueryTimeout(30);
    		ResultSet rs = stmt.executeQuery("select * from Orders");});
        db.createTableOrders();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:orders.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Orders");
		assertFalse(rs.next());
		conn.close();
	}
	
	@Test
	public void testAddOrder() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.clearTableOrders();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:orders.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Orders");
		assertFalse(rs.next());
		MyOrder op = new MyOrder(1,"ciao",1,2.0);
		db.addOrder(op);
		rs = stmt.executeQuery("select * from Orders");
		assertTrue(rs.next());
		db.createTableOrders();
		conn.close();
	}
	
	@Test
	public void testCreateTableProducts() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.destroyProducts();
        assertThrows(SQLiteException.class,()->{
        	Connection conn = null;
    		conn = DriverManager.getConnection("jdbc:sqlite:products.db");
    		Statement stmt = conn.createStatement();
    		stmt.setQueryTimeout(30);
    		ResultSet rs = stmt.executeQuery("select * from Products");});
        db.createTableProducts();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:products.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Products");
		assertFalse(rs.next());
		conn.close();
	}
	
	@Test
	public void testAddProduct() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.clearTableProducts();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:products.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Products");
		assertFalse(rs.next());
		db.addProduct(1,"aa","ciao",2.0,"note",1,"lol");
		rs = stmt.executeQuery("select * from Products");
		assertTrue(rs.next());
		db.createTableProducts();
		conn.close();
	}
	
	@Test
	public void testCreateTableEmployees() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.destroyEmployees();
        assertThrows(SQLiteException.class,()->{
        	Connection conn = null;
    		conn = DriverManager.getConnection("jdbc:sqlite:employees.db");
    		Statement stmt = conn.createStatement();
    		stmt.setQueryTimeout(30);
    		ResultSet rs = stmt.executeQuery("select * from Employees");});
        db.createTableEmployees();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:employees.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Employees");
		assertFalse(rs.next());
		conn.close();
	}
	
	@Test
	public void testAddEmployee() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.clearTableEmployees();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:employees.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Employees");
		assertFalse(rs.next());
		db.addUser(1,"aa","ciao","lol");
		rs = stmt.executeQuery("select * from Employees");
		assertTrue(rs.next());
		db.createTableEmployees();
		conn.close();
	}
	
	@Test
	public void testCreateTableCustomers() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.destroyCustomers();
        assertThrows(SQLiteException.class,()->{
        	Connection conn = null;
    		conn = DriverManager.getConnection("jdbc:sqlite:customers.db");
    		Statement stmt = conn.createStatement();
    		stmt.setQueryTimeout(30);
    		ResultSet rs = stmt.executeQuery("select * from Customers");});
        db.createTableCustomers();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:customers.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Customers");
		assertFalse(rs.next());
		conn.close();
	}
	
	@Test
	public void testAddCustomer() throws Exception {
		EZShop shop = null;
		DataBase db = new DataBase(shop);
		db.setRunning();
		db.clearTableCustomers();
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:customers.db");
		Statement stmt = conn.createStatement();
		stmt.setQueryTimeout(30);
		ResultSet rs = stmt.executeQuery("select * from Customers");
		assertFalse(rs.next());
		db.addCustomer(1,"aa","ciao",0);
		rs = stmt.executeQuery("select * from Customers");
		assertTrue(rs.next());
		db.createTableCustomers();
		conn.close();
	}
}
