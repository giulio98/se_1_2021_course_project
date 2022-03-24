package APItesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.CardCounter;
import it.polito.ezshop.data.CustomersCounter;
import it.polito.ezshop.data.DataBase;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EmployeesCounter;
import it.polito.ezshop.data.MySaleTransaction;
import it.polito.ezshop.data.OperationsCounter;
import it.polito.ezshop.data.ProductCounter;
import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRFIDException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class ApiTesting {
	//------------------------------------------------------------USER
	
	private EZShop e;
	private DataBase db;
	@Before
	public void beforeEach() throws Exception {
		CustomersCounter.setCounter(0);
		e = null;
		db = new DataBase(e);
		db.setRunning();
		db.clearTableCustomers();
		db.clearTableEmployees();
		db.clearTableOperations();
		db.clearTableEntries();
		db.clearTableProducts();
		db.clearTableSales();
		db.clearTableOrders();
		e = new EZShop();
		e.createUser("SuperUser", "1234", "Administrator");
	}
	@Test
	public void testRecoverBalanceOperation() {
		e.recoverBalanceOperation(-1, "Order", 2.0, LocalDate.of(2021, Month.APRIL, 12), "12:30", "OPENED");
	}
	@Test
	public void testRecoverSale() {
		e.recoverSale(-1, 0.5, 2.0, 10, "OPENED");
	} 
	@Test
	public void testRecoverTicket() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		e.login("SuperUser", "1234");
		Integer id=e.startSaleTransaction();
		e.recoverTicket(id, "1234567890", "ciao", 2, 1.0, 2.0, 0.5, 0);
	}
	@Test
	public void testReset() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		e.login("SuperUser", "1234");
		e.reset();
		assertEquals(0,e.getAllProductTypes().size());
		assertEquals(0,e.getCreditsAndDebits(null, null).size());
		assertEquals(0.0,e.computeBalance(),0);
		
	}
	
	@Test
	public void testCreateUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
		assertThrows(InvalidUsernameException.class,()->{
			e.createUser(null, "1234", "Cashier");
		});
		assertThrows(InvalidUsernameException.class,()->{
			e.createUser("", "1234", "Cashier");
		});
		assertThrows(InvalidPasswordException.class,()->{
			e.createUser("gianni", null, "Cashier");
		});
		assertThrows(InvalidPasswordException.class,()->{
			e.createUser("gianni", "", "Cashier");
		});
		InvalidRoleException exr=assertThrows(InvalidRoleException.class,()->{
			e.createUser("gianni", "1234", null);
		});
		assertEquals("Invalid role",exr.getMessage());
		assertThrows(InvalidRoleException.class,()->{
			e.createUser("gianni", "1234", "");
		});
		assertThrows(InvalidRoleException.class,()->{
			e.createUser("gianni", "1234", "NoRole");
		});
		EmployeesCounter.setCounter(0);
		e.login("SuperUser", "1234");
		assertEquals(new Integer(-1), e.createUser("SuperUser", "1234", "Administrator"));
		Integer cont;
		Integer id2=e.createUser("lollo", "1234", "Cashier");
		cont = ((Integer)EmployeesCounter.getCounter());
		assertEquals(cont,id2);
		Integer id3=e.createUser("gino", "1234", "ShopManager");
		assertEquals(EmployeesCounter.getCounter(),id3);
		
	}
	@Test
	public void testDeleteUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
		assertThrows(InvalidUserIdException.class,()->{
			e.deleteUser(-1);
		});
		assertThrows(InvalidUserIdException.class,()->{
			e.deleteUser(null);
		});
		assertThrows(UnauthorizedException.class,()->{
			e.deleteUser(1);
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.logout();
			e.login("gianni", "1234");
			e.deleteUser(1);
		});
		e.login("SuperUser", "1234");
		Integer id2=e.createUser("gianmarco", "1234", "Cashier");
		Integer id=EmployeesCounter.getCounter().intValue();
		assertFalse(e.deleteUser((Integer)(id+1)));
		assertTrue(e.deleteUser(id2));
		
	}
	
	@Test
	public void testGetAllUsers() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.getAllUsers();
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.logout();
			e.login("gianni", "1234"); //non è autorizzato perchè cashier
			e.getAllUsers();
		});
		e.login("SuperUser", "1234");
		assertEquals(2,e.getAllUsers().size()); //superuser + gianni
	}
	@Test
	public void testGetUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.getUser(1);
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.logout();
			e.login("gianni", "1234"); //non è autorizzato perchè cashier
			e.getUser(1);
		});
		InvalidUserIdException exu=assertThrows(InvalidUserIdException.class,()->{
			e.login("SuperUser", "1234"); //ha permesso
			e.getUser(null);
		});
		assertEquals("Invalid id", exu.getMessage());
		assertThrows(InvalidUserIdException.class,()->{
			e.login("SuperUser", "1234");
			e.getUser(-1);
		});
		e.login("SuperUser", "1234");
		Integer id1=e.createUser("Maurizio", "1234", "Administrator");
		e.login("Maurizio", "1234");
		e.getUser(id1);
	}
	@Test
	public void testUpdateUserRights() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
		UnauthorizedException exu=assertThrows(UnauthorizedException.class,()->{
			e.updateUserRights((Integer)1, "Cashier");
		});
		assertEquals("Unauthorized", exu.getMessage());
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.updateUserRights((EmployeesCounter.getCounter()), "Cashier");
		});
		assertThrows(InvalidUserIdException.class,()->{
			e.login("SuperUser", "1234");
			e.updateUserRights(null, "Cashier"); 
		});
		assertThrows(InvalidUserIdException.class,()->{
			e.login("SuperUser", "1234");
			e.updateUserRights(-1, "Cashier");
		});
		assertThrows(InvalidRoleException.class,()->{
			e.login("SuperUser", "1234");
			Integer id2=e.createUser("gianmarco", "1234", "Cashier");
			e.updateUserRights(id2, null);
		});
		assertThrows(InvalidRoleException.class,()->{
			e.login("SuperUser", "1234");
			Integer id2=e.createUser("prova", "1234", "Cashier");
			e.updateUserRights(id2, "NoRole");
		});
		assertThrows(InvalidRoleException.class,()->{
			e.login("SuperUser", "1234");
			Integer id2=e.createUser("gino", "1234", "Cashier");
			e.updateUserRights(id2, "");
		});
		e.login("SuperUser", "1234");
		Integer id2=e.createUser("prova2", "1234", "Cashier");
		assertFalse(e.updateUserRights(EmployeesCounter.getCounter()+1, "Administrator"));
		assertTrue(e.updateUserRights(id2, "Administrator"));
		assertTrue(e.updateUserRights(id2, "Cashier"));
		assertTrue(e.updateUserRights(id2, "ShopManager"));
	}

	@Test
	public void testLogin() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		InvalidUsernameException ex=assertThrows(InvalidUsernameException.class,()->{
			e.login(null, "1234");
		});
		assertEquals("Invalid Username",ex.getMessage());
		assertThrows(InvalidUsernameException.class,()->{
			e.login("", "1234");
		});
		InvalidPasswordException exp=assertThrows(InvalidPasswordException.class,()->{
			e.login("gianni", null);
		});
		assertEquals("Invalid Password",exp.getMessage());
		assertThrows(InvalidPasswordException.class,()->{
			e.login("gianni", "");
		});
		e.login("SuperUser", "1234");
		e.createUser("gianni", "1234","Cashier");
		assertEquals(null,e.login("giovanni", "1234"));
		assertNotEquals(null,e.login("gianni", "1234"));	
	}

	@Test
	public void testLogout() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		assertFalse(e.logout());
		e.login("SuperUser", "1234");
		e.createUser("gianni", "1234", "Cashier");
		e.login("gianni", "1234");
		assertTrue(e.logout());
	}
	@Test
	public void testDeleteUserByName() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, UnauthorizedException, InvalidRoleException {
		e.login("SuperUser", "1234");
		assertFalse(e.deleteUserByName("roberto", "1234"));
		e.createUser("roberto", "1234", "Cashier");
		assertTrue(e.deleteUserByName("roberto", "1234"));
		
	}


	//------------------------------------------------------------------PRODUCT
	@Test
	public void testCreateProductType() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.createProductType("pasta", "1234567890", 1.0, "cibo");
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.createProductType("pasta", "1234567890", 1.0, "cibo");
		});
		InvalidProductDescriptionException ed=assertThrows(InvalidProductDescriptionException.class,()->{
			e.login("SuperUser", "1234");
			e.createProductType(null, "1234567890", 1.0, "cibo");
		});
		assertEquals("Invalid description",ed.getMessage());
		assertThrows(InvalidProductDescriptionException.class,()->{
			e.login("SuperUser", "1234");
			e.createProductType("", "1234567890", 1.0, "cibo");
		});
		InvalidProductCodeException ep=assertThrows(InvalidProductCodeException.class,()->{
			e.createProductType("pasta", null, 1.0, "cibo");
		});
		assertEquals("Invalid code",ep.getMessage());
		assertThrows(InvalidProductCodeException.class,()->{
			e.createProductType("pasta", "", 1.0, "cibo");
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.createProductType("pasta", "1234", 1.0, "cibo");  //sbagliato numero cifre
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.createProductType("pasta", "9999999999999", 1.0, "cibo");
		});
		InvalidPricePerUnitException epr=assertThrows(InvalidPricePerUnitException.class,()->{
			e.createProductType("pasta", "1234567890128", -1.0, "cibo");
		});
		assertEquals("Invalid price",epr.getMessage());
		assertEquals(e.createProductType("pasta", "1234567890128", 1.0, "cibo"),ProductCounter.getCounter());
		assertEquals((Integer)(-1),e.createProductType("pasta", "1234567890128", 1.0, "cibo"));
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertEquals((Integer)(-1),e.createProductType("pasta", "1234567890128", 1.0, "cibo"));
		e.logout();
		e.login("SuperUser", "1234");
	}
	@Test
	public void testUpdateProduct() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.updateProduct(1,"pasta", "1234567890", 1.0, "cibo");
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.updateProduct(1,"pasta", "1234567890", 1.0, "cibo");
		});
		assertThrows(InvalidProductDescriptionException.class,()->{
			e.login("SuperUser", "1234");
			e.updateProduct(1,null, "1234567890", 1.0, "cibo");
		});
		assertThrows(InvalidProductDescriptionException.class,()->{
			e.login("SuperUser", "1234");
			e.updateProduct(1,"", "1234567890", 1.0, "cibo");
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.updateProduct(1,"pasta", null, 1.0, "cibo");
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.updateProduct(1,"pasta", "", 1.0, "cibo");
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.updateProduct(1,"pasta", "1234", 1.0, "cibo");
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.updateProduct(1,"pasta", "9999999999999", 1.0, "cibo");
		});
		assertThrows(InvalidPricePerUnitException.class,()->{
			e.updateProduct(1,"pasta", "1234567890128", -1.0, "cibo");
		});
		assertThrows(InvalidProductIdException.class,()->{
			e.updateProduct(null,"pasta", "1234567890128", 1.0, "cibo");
		});
		assertThrows(InvalidProductIdException.class,()->{
			e.updateProduct(-1,"pasta", "1234567890128", 1.0, "cibo");
		});
		assertFalse(e.updateProduct(ProductCounter.getCounter()+1,"pasta", "1234567890128", 1.0, "cibo"));
		Integer id=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		assertTrue(e.updateProduct(id, "ceci", "0987654321234", 1.0, "cibo"));
		Integer id2=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		//assertFalse(e.updateProduct(id2, "ceci", "0987654321234", 1.0, "cibo"));
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertFalse(e.updateProduct(id, "ceci", "1234567890128", 1.0, "cibo"));
		e.logout();
		e.login("SuperUser", "1234");
		
		
		
	}
	@Test
	public void testDeleteProductType() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		assertThrows(UnauthorizedException.class,()->{
			e.deleteProductType(1);
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.deleteProductType(1);
		});
		assertThrows(InvalidProductIdException.class,()->{
			e.login("SuperUser", "1234");
			e.deleteProductType(null);
		});
		assertThrows(InvalidProductIdException.class,()->{
			e.login("SuperUser", "1234");
			e.deleteProductType(-1);
		});
		e.login("SuperUser", "1234");
		assertFalse(e.deleteProductType(ProductCounter.getCounter()+1));
		Integer id=e.createProductType("pasta","1234567890128", 1.0, "cibo");
		assertTrue(e.deleteProductType(id));
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertFalse(e.deleteProductType(id));
		e.logout();
		e.login("SuperUser", "1234");
		
	}

	@Test
	public void testGetAllProductType() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.getAllProductTypes();
		});
		e.login("SuperUser", "1234");
		assertEquals(0,e.getAllProductTypes().size());
		e.createProductType("pasta","1234567890128", 1.0, "cibo");
		assertEquals(1,e.getAllProductTypes().size());
		
	}

	@Test
	public void testGetProductTypeByBarcode() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductCodeException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException {
		assertThrows(UnauthorizedException.class,()->{
			e.getProductTypeByBarCode("1234567890");
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.getProductTypeByBarCode("1234567890");
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.login("SuperUser", "1234");
			e.getProductTypeByBarCode(null);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.getProductTypeByBarCode("");
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.getProductTypeByBarCode("1234");
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.getProductTypeByBarCode("1234567890123");
		});
		assertEquals(null,e.getProductTypeByBarCode("1234567890128"));
		e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		assertNotEquals(null,e.getProductTypeByBarCode("1234567890128"));
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertNotEquals(null,e.getProductTypeByBarCode("1234567890128"));
		e.logout();
		e.login("SuperUser", "1234");
		
	}

	@Test
	public void testGetProductTypesByDescription() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.getProductTypesByDescription("pasta");
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.getProductTypesByDescription("pasta");
		});
		e.login("SuperUser", "1234");
		assertEquals(0, e.getProductTypesByDescription("pasta").size());
		e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		assertEquals(1, e.getProductTypesByDescription("pasta").size());
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertEquals(1, e.getProductTypesByDescription("pasta").size());
		e.logout();
		e.login("SuperUser", "1234");
		
	}

	@Test
	public void testUpdateQuantity() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
		assertThrows(UnauthorizedException.class,()->{
			e.updateQuantity(1, 1);
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.updateQuantity(1, 1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidProductIdException.class,()->{
			e.updateQuantity(null, 1);
		});
		assertThrows(InvalidProductIdException.class,()->{
			e.updateQuantity(-1, 1);
		});
		assertFalse(e.updateQuantity(ProductCounter.getCounter()+1, 1));
		Integer id=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		assertFalse(e.updateQuantity(id, 1));
		e.updatePosition(id, "1-a-1");
		assertFalse(e.updateQuantity(id, -10));
		assertTrue(e.updateQuantity(id, 10));
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertTrue(e.updateQuantity(id, 10));
		e.logout();
		e.login("SuperUser", "1234");
	}

	@Test
	public void testUpdatePosition() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		assertThrows(UnauthorizedException.class,()->{
			e.updatePosition(1, "1-a-1");
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.updatePosition(1, "1-a-1");
		});
		e.login("SuperUser", "1234");
		InvalidProductIdException exp=assertThrows(InvalidProductIdException.class,()->{
			e.updatePosition(null, "1-a-1");
		});
		assertEquals("Invalid id", exp.getMessage());
		assertThrows(InvalidProductIdException.class,()->{
			e.updatePosition(-1, "1-a-1");
		});
		InvalidLocationException el=assertThrows(InvalidLocationException.class,()->{
			Integer id1=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
			e.updatePosition(id1, "1-2-1");
		});
		assertEquals("Invalid location",el.getMessage());
		db.clearTableProducts();
		e = new EZShop();
		e.createUser("SuperUser", "1234", "Administrator");
		e.login("SuperUser", "1234");
		assertFalse(e.updatePosition(ProductCounter.getCounter()+1, "1-a-1"));
		Integer id=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		assertTrue(e.updatePosition(id, null));
		assertTrue(e.updatePosition(id, ""));
		assertTrue(e.updatePosition(id, "1-a-1"));
		Integer id2=e.createProductType("ceci", "0987654321128", 1.0, "cibo");
		assertFalse(e.updatePosition(id2, "1-a-1"));
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertFalse(e.updatePosition(id2, "1-a-1"));
		e.logout();
		e.login("SuperUser", "1234");
	}


	//-----------------------------------------------------------------------------------------ORDERS
	@Test
	public void testIssueOrder() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductDescriptionException {
		assertThrows(UnauthorizedException.class,()->{
			e.issueOrder("1234567890", 1, 1.0);
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.issueOrder("1234567890", 1, 1.0);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidProductCodeException.class,()->{
			e.issueOrder("1234567890", 1, 1.0);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.issueOrder(null, 1, 1.0);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.issueOrder("", 1, 1.0);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.issueOrder("1234", 1, 1.0);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.issueOrder("1234567890123", 1, 1.0);
		});
		assertThrows(InvalidQuantityException.class,()->{
			e.issueOrder("1234567890128", -1, 1.0);
		});
		assertThrows(InvalidPricePerUnitException.class,()->{
			e.issueOrder("1234567890128", 1, -1.0);
		});
		assertEquals((Integer)(-1),e.issueOrder("1234567890128", 1, 1.0));
		e.createProductType("pasta","1234567890128", 1.0, "cibo");
		assertEquals(e.issueOrder("1234567890128", 1, 1.0),OperationsCounter.getCounter());	
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertEquals(e.issueOrder("1234567890128", 1, 1.0),OperationsCounter.getCounter());	
		e.logout();
		e.login("SuperUser", "1234");
	}
	@Test
	public void testRecoverOrder() {
		assertTrue(e.recoverOrder(new Integer(OperationsCounter.getCounter()+1), "1234567890128", 1.0, 1, "COMPLETED"));
	}


	@Test
	public void testPayOrderFor() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductDescriptionException {
		assertThrows(UnauthorizedException.class,()->{
			e.payOrderFor("1234567890", 1, 1.0);
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.payOrderFor("1234567890", 1, 1.0);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidProductCodeException.class,()->{
			e.payOrderFor("1234567890", 1, 1.0);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.payOrderFor(null, 1, 1.0);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.payOrderFor("", 1, 1.0);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.payOrderFor("1234567890", 1, 1.0);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.payOrderFor("1234567890123", 1, 1.0);
		});
		InvalidQuantityException eq=assertThrows(InvalidQuantityException.class,()->{
			e.payOrderFor("1234567890128", -1, 1.0);
		});
		assertEquals("Invalid quantity",eq.getMessage());
		assertThrows(InvalidPricePerUnitException.class,()->{
			e.payOrderFor("1234567890128", 1, -1.0);
		});
		assertEquals((Integer)(-1),e.payOrderFor("1234567890128", 1, 1.0));
		e.createProductType("pasta","1234567890128", 1.0, "cibo");
		assertEquals((Integer)(-1), e.payOrderFor("1234567890128", 1, 1.0)); //balance non sufficente
		e.recordBalanceUpdate(10.0); //id 1
		assertEquals(new Integer(OperationsCounter.getCounter()+1),e.payOrderFor("1234567890128", 1, 1.0)); //id 2	
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertEquals(new Integer(OperationsCounter.getCounter()+1),e.payOrderFor("1234567890128", 1, 1.0)); //id 2	
		e.logout();
		e.login("SuperUser", "1234");
	}


	@Test
	public void testPayOrder() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidOrderIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException {
		assertThrows(UnauthorizedException.class,()->{
			e.payOrder(1);
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.payOrder(1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidOrderIdException.class,()->{
			e.payOrder(null);
		});
		assertThrows(InvalidOrderIdException.class,()->{
			e.payOrder(-1);
		});
		assertFalse(e.payOrder(OperationsCounter.getCounter()+1));
		e.createProductType("pasta","1234567890128", 1.0, "cibo");
		e.recordBalanceUpdate(10.0); //id 1
		Integer id=e.issueOrder("1234567890128", 1, 1.0);
		assertTrue(e.payOrder(id));
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		Integer id1=e.issueOrder("1234567890128", 1, 1.0);
		assertTrue(e.payOrder(id1));
		e.logout();
		e.login("SuperUser", "1234");
		
	}


	@Test
	public void testRecordOrderArrival() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException {
		assertThrows(UnauthorizedException.class,()->{
			e.recordOrderArrival(1);
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.recordOrderArrival(1);
		});
		e.login("SuperUser", "1234");
		InvalidOrderIdException eo=assertThrows(InvalidOrderIdException.class,()->{
			e.recordOrderArrival(null);
		});
		assertEquals("Invalid id", eo.getMessage());
		assertThrows(InvalidOrderIdException.class,()->{
			e.recordOrderArrival(-1);
		});
		Integer pid = e.createProductType("pasta","1234567890128", 1.0, "cibo");
		assertThrows(InvalidLocationException.class,()->{
			Integer id=e.issueOrder("1234567890128", 1, 1.0);
			e.recordOrderArrival(id);
		});
		Integer oid=e.issueOrder("1234567890128", 1, 1.0);
		e.updatePosition(pid, "1-a-1");
		e.recordBalanceUpdate(10.0); //id 1
		e.payOrder(oid);
		assertTrue(e.recordOrderArrival(oid));	
		assertFalse(e.recordOrderArrival((Integer)99));
		Integer oid1=e.issueOrder("1234567890128", 1, 1.0);
		e.updatePosition(pid, "1-a-1");
		e.recordBalanceUpdate(10.0); //id 1
		assertFalse(e.recordOrderArrival(oid1));
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertFalse(e.recordOrderArrival((Integer)99));
		e.logout();
		e.login("SuperUser", "1234");
	}


	@Test
	public void testGetAllOrders() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException {
		assertThrows(UnauthorizedException.class,()->{
			e.getAllOrders();
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.getAllOrders();
		});
		e.login("SuperUser", "1234");
		e.createProductType("pasta","1234567890128", 1.0, "cibo");
		assertEquals(0,e.getAllOrders().size());
		e.issueOrder("1234567890128", 1, 1.0);
		assertEquals(1,e.getAllOrders().size());
		e.createUser("france", "1234", "ShopManager");
		e.logout();
		e.login("france", "1234");
		assertEquals(1,e.getAllOrders().size());
		e.logout();
		e.login("SuperUser", "1234");
	}


	//----------------------------------------------------------------------------------------------------------------CUSTOMERS
	@Test
	public void testDefineCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.defineCustomer("andrea");
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidCustomerNameException.class,()->{
			e.defineCustomer(null);
		});
		assertThrows(InvalidCustomerNameException.class,()->{
			e.defineCustomer("");
		});
		Integer id1=e.defineCustomer("andrea");
		assertEquals(CustomersCounter.getCounter(),id1);
		assertEquals((Integer)(-1),e.defineCustomer("andrea"));			
	}

	@Test
	public void testModifyCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.modifyCustomer(1, "andrea", "100000001");
		});
		e.login("SuperUser", "1234");
		InvalidCustomerNameException ecn=assertThrows(InvalidCustomerNameException.class,()->{
			e.modifyCustomer(1, null, "100000001");
		});
		assertEquals("Invalid name",ecn.getMessage());
		assertThrows(InvalidCustomerNameException.class,()->{
			e.modifyCustomer(1, "", "100000001");
		});
		assertThrows(InvalidCustomerIdException.class,()->{
			e.modifyCustomer(null, "andrea", "100000001");
		});
		assertThrows(InvalidCustomerIdException.class,()->{
			e.modifyCustomer(-1, "andrea", "100000001");
		});
		assertThrows(InvalidCustomerCardException.class,()->{
			e.modifyCustomer(1, "andrea", "1234");
		});
		assertFalse(e.modifyCustomer(2, "andrea", null));
		Integer id=e.defineCustomer("andrea");
		assertTrue(e.modifyCustomer(id, "andrea", null));
		assertTrue(e.modifyCustomer(id, "andrea", "1000000001"));
		assertTrue(e.modifyCustomer(id, "andrea", ""));
		assertFalse(e.modifyCustomer(CustomersCounter.getCounter()+1, "gino", "")); //true true ->buono,buono fatto
													   //false true -> 
													//true false

	}
	
	@Test
	public void testDeleteCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException {
		assertThrows(UnauthorizedException.class,()->{
			e.deleteCustomer(1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidCustomerIdException.class,()->{
			e.deleteCustomer(null);
		});
		assertThrows(InvalidCustomerIdException.class,()->{
			e.deleteCustomer(-1);
		});
		assertFalse(e.deleteCustomer(CustomersCounter.getCounter()+1));
		Integer id1= e.defineCustomer("andrea");
		assertTrue(e.deleteCustomer(id1));
	}


	@Test
	public void testGetCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException {
		assertThrows(UnauthorizedException.class,()->{
			e.getCustomer(1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidCustomerIdException.class,()->{
			e.getCustomer(null);
		});
		assertThrows(InvalidCustomerIdException.class,()->{
			e.getCustomer(-1);
		});
		//assertEquals(null,e.getCustomer(CustomersCounter.getCounter()));
		Integer id1= e.defineCustomer("andrea");
		assertNotEquals(null,e.getCustomer(id1));
		assertEquals(id1,e.getCustomer(id1).getId());
	}


	@Test
	public void testGetAllCustomers() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.getAllCustomers();
		});
		e.login("SuperUser", "1234");
		assertEquals(0,e.getAllCustomers().size());
		e.defineCustomer("andrea");
		assertEquals(1,e.getAllCustomers().size());
	}

	@Test
	public void testCreateCard() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.createCard();
		});
		e.login("SuperUser", "1234");
		String c=e.createCard();
		assertEquals(CardCounter.getCounter(),c);
		assertTrue(c.length() == 10); 
	}


	@Test
	public void testAttachCardToCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException, InvalidCustomerNameException {
		assertThrows(UnauthorizedException.class,()->{
			e.attachCardToCustomer("10000001", 1);
		});
		e.login("SuperUser", "1234");
		InvalidCustomerIdException eci=assertThrows(InvalidCustomerIdException.class,()->{
			e.attachCardToCustomer("10000001", null);
		});
		assertEquals("Invalid id", eci.getMessage());
		assertThrows(InvalidCustomerIdException.class,()->{
			e.attachCardToCustomer("10000001", -1);
		});
		InvalidCustomerCardException ec=assertThrows(InvalidCustomerCardException.class,()->{
			e.attachCardToCustomer(null, 1);
		});
		assertEquals("Invalid card", ec.getMessage());
		assertThrows(InvalidCustomerCardException.class,()->{
			e.attachCardToCustomer("", 1);
		});
		assertThrows(InvalidCustomerCardException.class,()->{
			e.attachCardToCustomer("1234", 1);
		});
		assertFalse(e.attachCardToCustomer("1234567890", CustomersCounter.getCounter()+1)); //dovrebbe ritornare false perchè la carta non esiste ( e neanche il customer)
		Integer cid=e.defineCustomer("andrea");
		CardCounter.setCounter("1000000000");
		String card=e.createCard();
		assertTrue(e.attachCardToCustomer(card, cid));
	}


	@Test
	public void testModifyPointsOnCard() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerCardException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerIdException {
		assertThrows(UnauthorizedException.class,()->{
			e.modifyPointsOnCard("1234567890", 100);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidCustomerCardException.class,()->{
			e.modifyPointsOnCard(null, 100);
		});
		assertThrows(InvalidCustomerCardException.class,()->{
			e.modifyPointsOnCard("", 100);
		});
		assertThrows(InvalidCustomerCardException.class,()->{
			e.modifyPointsOnCard("1234", 100);
		});
		assertFalse(e.modifyPointsOnCard("1234567890", 100)); //la carta non esiste
		String card = e.createCard();
		Integer cid = e.defineCustomer("andrea");
		e.attachCardToCustomer(card, cid);
		assertTrue(e.modifyPointsOnCard(card, 100));
	}


	//----------------------------------------------------------------------------------------------------SALE TRANSACTION
	@Test
	public void testStartSaleTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.startSaleTransaction();
		});
		e.login("SuperUser", "1234");
		Integer id=e.startSaleTransaction();
		assertEquals(id,OperationsCounter.getCounter());
	}

	@Test
	public void testAddProductToSale() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {
		assertThrows(UnauthorizedException.class,()->{
			e.addProductToSale(1, "1234567890",1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidQuantityException.class,()->{
			e.addProductToSale(1, "1234567890", -1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.addProductToSale(1, null, 1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.addProductToSale(1, "", 1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.addProductToSale(1, "1234", 1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.addProductToSale(1, "1234567890123", 1);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.addProductToSale(null, "1234567890128", 1);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.addProductToSale(-1, "1234567890128", 1);
		});
		assertFalse(e.addProductToSale(1, "1234567890128", 1)); //product non esiste
		Integer idp=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		e.updatePosition(idp, "1-a-1");
		e.updateQuantity(idp, 1);
		assertFalse(e.addProductToSale(1, "1234567890128", 2)); //quantita non disp
		assertFalse(e.addProductToSale(1, "1234567890128", 1)); //transaction non esiste
		Integer idt=e.startSaleTransaction();
		assertTrue(e.addProductToSale(idt, "1234567890128", 1));
	}

	@Test
	public void testDeleteProductFromSale() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {
		assertThrows(UnauthorizedException.class,()->{
			e.deleteProductFromSale(1, "1234567890", 1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidQuantityException.class,()->{
			e.deleteProductFromSale(1, "1234567890", -1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.deleteProductFromSale(1, null, 1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.deleteProductFromSale(1, "", 1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.deleteProductFromSale(1, "1234", 1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.deleteProductFromSale(1, "1234567890123", 1);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.deleteProductFromSale(null, "1234567890128", 1);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.deleteProductFromSale(-1, "1234567890128", 1);
		});
		assertFalse(e.deleteProductFromSale(1, "1234567890128", 1));
		Integer idp=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		e.updatePosition(idp, "1-a-1");
		e.updateQuantity(idp, 1);
		assertFalse(e.deleteProductFromSale(1, "1234567890128", 1)); //transaction non esiste
		Integer idt=e.startSaleTransaction();
		e.addProductToSale(idt, "1234567890128", 1);
		assertTrue(e.deleteProductFromSale(idt, "1234567890128", 1));	
	}

	@Test
	public void testApplyDiscountRateToProduct() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException {
		assertThrows(UnauthorizedException.class,()->{
			e.applyDiscountRateToProduct(1, "1234567890", 0.5);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.applyDiscountRateToProduct(null, "1234567890", 0.5);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.applyDiscountRateToProduct(-1, "1234567890", 0.5);
		});
		assertThrows(InvalidDiscountRateException.class,()->{
			e.applyDiscountRateToProduct(1, "1234567890", -0.5);
		});
		assertThrows(InvalidDiscountRateException.class,()->{
			e.applyDiscountRateToProduct(1, "1234567890", 1.2);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.applyDiscountRateToProduct(1, null, 0.5);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.applyDiscountRateToProduct(1, "", 0.5);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.applyDiscountRateToProduct(1, "1234", 0.5);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.applyDiscountRateToProduct(1, "1234567890123", 0.5);
		});
		assertFalse(e.applyDiscountRateToProduct(1, "1234567890128", 0.5)); //no prod, no sale
		Integer idp=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		e.updatePosition(idp, "1-a-1");
		e.updateQuantity(idp, 5);
		assertFalse(e.applyDiscountRateToProduct(OperationsCounter.getCounter(),"1234567890128", 0.5)); //no sale
		Integer ids=e.startSaleTransaction(); //anto??? giulio:verificare quando non è opened -> attivare ad esempio pagamento
		assertFalse(e.addProductToSale(ids,"1234567890128", 6)); //solo 1 c'è nel catalogue
		e.addProductToSale(ids,"1234567890128", 1);
		assertTrue(e.applyDiscountRateToProduct(ids, "1234567890128", 0.5));
		e.endSaleTransaction(ids);
		assertFalse(e.applyDiscountRateToProduct(ids, "1234567890128", 0.5));
		assertFalse(e.applyDiscountRateToProduct(OperationsCounter.getCounter()+1, "1234567890128", 0.5));
	}

	@Test
	public void testApplyDiscountRateToSale() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.applyDiscountRateToSale(1, 0.5);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.applyDiscountRateToSale(null, 0.5);
		});
		
		assertThrows(InvalidTransactionIdException.class,()->{
			e.applyDiscountRateToSale(-1, 0.5);
		});
		InvalidDiscountRateException ed=assertThrows(InvalidDiscountRateException.class,()->{
			e.applyDiscountRateToSale(1, -0.5);
		});
		assertEquals("Invalid discount",ed.getMessage());
		assertThrows(InvalidDiscountRateException.class,()->{
			e.applyDiscountRateToSale(1, 1.5);
		});
		assertFalse(e.applyDiscountRateToSale(1, 0.5)); //non esiste
		Integer ids=e.startSaleTransaction();
		assertTrue(e.applyDiscountRateToSale(ids, 0.5));
		e.endSaleTransaction(ids);
		assertFalse(e.applyDiscountRateToSale(ids, 0.5));
	}  
	
	@Test
	public void testComputePointsForSale() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.computePointsForSale(1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.computePointsForSale(null);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.computePointsForSale(-1);
		});
		assertEquals(-1,e.computePointsForSale(OperationsCounter.getCounter()+1));
		Integer ids=e.startSaleTransaction();
		assertEquals(0,e.computePointsForSale(ids));
		
	}

	@Test
	public void testEndSaleTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
		assertThrows(UnauthorizedException.class,()->{
			e.endSaleTransaction(1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.endSaleTransaction(null);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.endSaleTransaction(-1);
		});
		assertFalse(e.endSaleTransaction(OperationsCounter.getCounter()+1));
		/*VERIFICARE CLOSED E PAYED*/
		Integer ids=e.startSaleTransaction();
		assertTrue(e.endSaleTransaction(ids));	
		assertFalse(e.endSaleTransaction(ids));
		assertFalse(e.endSaleTransaction(ids));
		e.receiveCashPayment(ids, 50);
		assertFalse(e.endSaleTransaction(ids));
	}

	@Test
	public void testDeleteSaleTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidCreditCardException {
		assertThrows(UnauthorizedException.class,()->{
			e.deleteSaleTransaction(1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.deleteSaleTransaction(null);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.deleteSaleTransaction(-1);
		});
		assertFalse(e.deleteSaleTransaction(OperationsCounter.getCounter()+1));
		Integer ids=e.startSaleTransaction();
		assertTrue(e.deleteSaleTransaction(ids));
		ids=e.startSaleTransaction();
		e.endSaleTransaction(ids);
		MySaleTransaction sale = (MySaleTransaction) e.getSaleTransaction(ids);
		sale.setStatus("PAYED");
		assertFalse(e.deleteSaleTransaction(ids));
	}


	@Test
	public void testGetSaleTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.getSaleTransaction(1);
		});
		e.login("SuperUser", "1234");
		InvalidTransactionIdException eid=assertThrows(InvalidTransactionIdException.class,()->{
			e.getSaleTransaction(null);
		});
		assertEquals("Invalid id",eid.getMessage());
		assertThrows(InvalidTransactionIdException.class,()->{
			e.getSaleTransaction(-1);
		});
		e.login("SuperUser", "1234");
		assertEquals(null,e.getSaleTransaction(OperationsCounter.getCounter()));
		//verificato se issued, payed e closed
		Integer ids=e.startSaleTransaction();
		assertEquals(null,e.getSaleTransaction(ids));
		e.endSaleTransaction(ids);
		assertNotEquals(null,e.getSaleTransaction(ids));
		MySaleTransaction sale = (MySaleTransaction) e.getSaleTransaction(ids);
		sale.setStatus("PAYED");
		assertNotEquals(null,e.getSaleTransaction(ids));
	}


	//------------------------------------------------------------------------------------------------RETURN
	
	@Test
	public void testStartReturnTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.startReturnTransaction(1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.startReturnTransaction(null);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.startReturnTransaction(-1);
		});
		assertEquals((Integer)(-1),e.startReturnTransaction(OperationsCounter.getCounter()+1));
		Integer ids=e.startSaleTransaction();
		e.endSaleTransaction(ids);
		MySaleTransaction sale = (MySaleTransaction) e.getSaleTransaction(ids);
		assertEquals(new Integer(-1),e.startReturnTransaction(ids));
		sale.setStatus("PAYED");
		Integer idr=e.startReturnTransaction(ids);
		assertEquals(OperationsCounter.getCounter(),idr);
	}


	@Test
	public void testReturnProduct() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {
		assertThrows(UnauthorizedException.class,()->{
			e.returnProduct(1, "1234567890", 1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.returnProduct(null, "1234567890", 1);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.returnProduct(-1, "1234567890", 1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.returnProduct(1, null, 1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.returnProduct(1, "", 1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.returnProduct(1, "1234", 1);
		});
		assertThrows(InvalidProductCodeException.class,()->{
			e.returnProduct(1, "1234567890123", 1);
		});
		assertThrows(InvalidQuantityException.class,()->{
			e.returnProduct(1, "1234567890128", -1);
		});
		assertFalse(e.returnProduct(OperationsCounter.getCounter(), "1234567890128", 1));
		Integer ids=e.startSaleTransaction();
		e.endSaleTransaction(ids);
		MySaleTransaction sale = (MySaleTransaction) e.getSaleTransaction(ids);
		sale.setStatus("PAYED");
		Integer idr=e.startReturnTransaction(ids);
		assertFalse(e.returnProduct(idr, "1234567890128", 1));
		Integer pid = e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		e.updatePosition(pid, "1-a-1");
		e.updateQuantity(pid, 1);
		sale.setStatus("OPENED");
		e.addProductToSale(ids, "1234567890128", 1);
		e.endSaleTransaction(ids);
		sale.setStatus("PAYED");
		idr=e.startReturnTransaction(ids);
		assertTrue(e.returnProduct(idr, "1234567890128", 1));
	}


	@Test
	public void testEndReturnTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.endReturnTransaction(1, true);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.endReturnTransaction(null, true);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.endReturnTransaction(-1, true);
		});
		assertFalse(e.endReturnTransaction(OperationsCounter.getCounter()+1, true)); //ret non esiste
		Integer ids=e.startSaleTransaction();
		e.endSaleTransaction(ids);
		MySaleTransaction sale = (MySaleTransaction) e.getSaleTransaction(ids);
		sale.setStatus("PAYED");
		Integer idr=e.startReturnTransaction(ids);
		assertTrue(e.endReturnTransaction(idr,true));
		assertFalse(e.endReturnTransaction(idr,true)); //quando già chiusa
		idr=e.startReturnTransaction(ids);
		assertTrue(e.endReturnTransaction(idr,false));
	}


	@Test
	public void testDeleteReturnTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.deleteReturnTransaction(1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.deleteReturnTransaction(null);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.deleteReturnTransaction(-1);
		});
		assertFalse(e.deleteReturnTransaction(OperationsCounter.getCounter()+1));
		Integer ids=e.startSaleTransaction();
		e.endSaleTransaction(ids);
		MySaleTransaction sale = (MySaleTransaction) e.getSaleTransaction(ids);
		sale.setStatus("PAYED");
		Integer idr=e.startReturnTransaction(ids);
		assertFalse(e.deleteReturnTransaction(idr)); //è opened
		e.endReturnTransaction(idr,true);
		assertTrue(e.deleteReturnTransaction(idr)); //ora è closed
		//e se issued?
	}
	
	
	//-----------------------------------------------------------------------------------------------PAYMENT
	@Test
	public void testReceiveCashPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException {
		assertThrows(UnauthorizedException.class,()->{
			e.receiveCashPayment(1, 12.5);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.receiveCashPayment(null, 12.5); 
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.receiveCashPayment(-1, 12.5);
		});
		InvalidPaymentException ep=assertThrows(InvalidPaymentException.class,()->{
			e.receiveCashPayment(1, -1);
		});
		assertEquals("Invalid payment", ep.getMessage());
		assertThrows(InvalidPaymentException.class,()->{
			e.receiveCashPayment(1, -1);
		});
		assertEquals(-1,e.receiveCashPayment(OperationsCounter.getCounter()+1, 12.4),0);
		Integer ids=e.startSaleTransaction();
		Integer idp=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		e.updatePosition(idp, "1-a-1");
		e.updateQuantity(idp, 10); 
		e.addProductToSale(ids, "1234567890128", 2);
		assertEquals(-1,e.receiveCashPayment(ids, 12.4),0); //non closed
		e.endSaleTransaction(ids);
		assertEquals(-1,e.receiveCashPayment(ids, 1.0),0); //cash non sufficente
		assertEquals(2.0,e.receiveCashPayment(ids, 4.0),0);
		assertEquals(-1,e.receiveCashPayment(ids, 50),0);
		MySaleTransaction sale = (MySaleTransaction) e.getSaleTransaction(ids);
		assertEquals(sale.getStatus(),"PAYED");
	}

	@Test
	public void testReceiveCreditCardPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException {
		assertThrows(UnauthorizedException.class,()->{
			e.receiveCreditCardPayment(1, "1234");
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.receiveCreditCardPayment(null, "1234");
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.receiveCreditCardPayment(-1, "1234");
		});
		assertThrows(InvalidCreditCardException.class,()->{
			e.receiveCreditCardPayment(1, null);
		});
		assertThrows(InvalidCreditCardException.class,()->{
			e.receiveCreditCardPayment(1, ""); 
		});
		assertThrows(InvalidCreditCardException.class,()->{
			e.receiveCreditCardPayment(1, "1234567890123456");
		});
		//assertFalse(e.receiveCreditCardPayment(OperationsCounter.getCounter()+1, "1234")); //carta non esiste e sale non esiste
		assertFalse(e.receiveCreditCardPayment(OperationsCounter.getCounter()+1, "4716258050958645")); //sale non esiste
		Integer ids=e.startSaleTransaction();
		Integer idp=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		e.updatePosition(idp, "1-a-1");
		e.updateQuantity(idp, 10000);
		e.addProductToSale(ids, "1234567890128", 2);
		assertFalse(e.receiveCreditCardPayment(ids, "5100293991053009")); //non closed
		e.endSaleTransaction(ids);
		//assertFalse(e.receiveCreditCardPayment(ids, "1234")); //carta non esiste
		assertFalse(e.receiveCreditCardPayment(ids, "4716258050958645"));
		assertTrue(e.receiveCreditCardPayment(ids, "5100293991053009")); //tutto ok (anchr credito)
		assertFalse(e.receiveCreditCardPayment(ids, "5100293991053009"));
		ids=e.startSaleTransaction();
		e.addProductToSale(ids, "1234567890128", 1000);
		assertFalse(e.receiveCreditCardPayment(ids, "5100293991053009")); //credito non sufficiente sulla carta
		
	}

	@Test
	public void testReturnCashPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException, InvalidPaymentException {
		assertThrows(UnauthorizedException.class,()->{
			e.returnCashPayment(1);
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.returnCashPayment(null);
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.returnCashPayment(-1);
		});
		assertEquals(-1.0,e.returnCashPayment(OperationsCounter.getCounter()),0); //no ret
		Integer ids=e.startSaleTransaction();
		Integer idp=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		e.updatePosition(idp, "1-a-1");
		e.updateQuantity(idp, 10);
		e.addProductToSale(ids, "1234567890128",2);
		e.endSaleTransaction(ids);
		e.receiveCashPayment(ids, 20); //bilancio = +20
		Integer idr=e.startReturnTransaction(ids);
		MySaleTransaction sale = (MySaleTransaction) e.getSaleTransaction(ids);
		e.returnProduct(idr, "1234567890128", 2);
		e.endReturnTransaction(idr, true); //sale è stata aggiornata
		assertEquals(2.0,e.returnCashPayment(idr),0);
		assertEquals(-1.0,e.returnCashPayment(idr),0);
	}

	@Test
	public void testReturnCreditCardPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidPaymentException {
		assertThrows(UnauthorizedException.class,()->{
			e.returnCreditCardPayment(1,"1234");
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.returnCreditCardPayment(null,"1234");
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.returnCreditCardPayment(-1,"1234");
		});
		InvalidCreditCardException ec=assertThrows(InvalidCreditCardException.class,()->{
			e.returnCreditCardPayment(1,null);
		});
		assertEquals("Invalid card",ec.getMessage());
		assertThrows(InvalidCreditCardException.class,()->{
			e.returnCreditCardPayment(1,"");
		});
		assertThrows(InvalidCreditCardException.class,()->{
			e.returnCreditCardPayment(1, "1234567890123456");
		});
		assertEquals(-1.0,e.returnCreditCardPayment(OperationsCounter.getCounter(),"5100293991053009"),0); //ret non esiste
		Integer ids=e.startSaleTransaction();
		Integer idp=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		e.updatePosition(idp, "1-a-1");
		e.updateQuantity(idp, 10);
		e.addProductToSale(ids, "1234567890128",2);
		e.endSaleTransaction(ids);
		e.receiveCashPayment(ids, 20);
		Integer idr=e.startReturnTransaction(ids);
		e.returnProduct(idr, "1234567890128", 2);
		e.endReturnTransaction(idr, true);
		assertThrows(InvalidCreditCardException.class,()->{
			e.returnCreditCardPayment(idr, "1234");
		});
		//assertEquals(-1.0,e.returnCreditCardPayment(idr,"1234"),0); //carta non esistente/valida
		assertEquals(2.0,e.returnCreditCardPayment(idr,"5100293991053009"),0);
		assertEquals(-1.0,e.returnCreditCardPayment(idr,"5100293991053009"),0);
	}

	//--------------------------------------------------------------------------BALANCE
	@Test
	public void testRecordBalanceUpdate() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.recordBalanceUpdate(1.0);
		});
		e.login("SuperUser", "1234");
		assertFalse(e.recordBalanceUpdate(-1000.0));
		assertTrue(e.recordBalanceUpdate(1.0));
	}

	@Test
	public void testGetCreditsAndDebits() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.getCreditsAndDebits(LocalDate.of(2021, Month.MAY, 24), LocalDate.of(2021, Month.MAY, 26));
		});
		e.login("SuperUser", "1234");
		assertEquals(0,e.getCreditsAndDebits(LocalDate.of(2021, Month.MAY, 24), LocalDate.of(2021, Month.MAY, 26)).size());
		e.recordBalanceUpdate(20.0);
		e.recordBalanceUpdate(-10.0);
		assertEquals(2,e.getCreditsAndDebits(LocalDate.of(2021, Month.JULY, 26), LocalDate.of(2021, Month.MAY, 24)).size()); //invertite 
		assertEquals(2,e.getCreditsAndDebits(null, LocalDate.of(2021, Month.JULY, 26)).size());
		assertEquals(2,e.getCreditsAndDebits(LocalDate.of(2021, Month.MAY, 24), null).size());
		assertEquals(2,e.getCreditsAndDebits(null, null).size());
		// null null invertite
		// no-null null invertite
		// null no-null invertite
		// no-null no-null invertite
		
	}
	
	@Test
	public void testComputeBalance() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		assertThrows(UnauthorizedException.class,()->{
			e.computeBalance();
		});
		e.login("SuperUser", "1234");
		e.recordBalanceUpdate(20.0);
		e.recordBalanceUpdate(-10.0);
		assertEquals(10.0,e.computeBalance(),0);
	}
	@Test
	public void testRecordOrderArrivalRFID() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException, InvalidRFIDException {
		assertThrows(UnauthorizedException.class,()->{
			e.recordOrderArrivalRFID(1,"123456789000");
		});
		assertThrows(UnauthorizedException.class,()->{
			e.login("SuperUser", "1234");
			e.createUser("gianni", "1234", "Cashier");
			e.login("gianni", "1234");
			e.recordOrderArrivalRFID(1,"123456789000");
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidOrderIdException.class,()->{
			e.recordOrderArrivalRFID(null,"123456789000");
		});
		assertThrows(InvalidOrderIdException.class,()->{
			e.recordOrderArrivalRFID(-1,"123456789000");
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.createUser("gianniShop", "1234", "ShopManager");
			e.login("gianniShop", "1234");
			e.recordOrderArrivalRFID(1,null);
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.recordOrderArrivalRFID(1,"");
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.recordOrderArrivalRFID(1,"123456789");
		});
		assertThrows(InvalidLocationException.class,()->{
			Integer pid = e.createProductType("pasta","1234567890128", 1.0, "cibo");
			Integer oid=e.issueOrder("1234567890128", 1, 1.0);
			e.recordOrderArrivalRFID(oid,"123456789000"); 
			
		}); 
		Integer pid=e.getProductTypeByBarCode("1234567890128").getId();
		assertFalse(e.recordOrderArrivalRFID(OperationsCounter.getCounter()+1,"123456789000"));
		Integer oid=e.issueOrder("1234567890128", 3, 1.0);
		e.updatePosition(pid, "1-a-1");
		assertFalse(e.recordOrderArrivalRFID(oid, "123456789000"));
		e.recordBalanceUpdate(10.0); 
		e.payOrder(oid);
		assertTrue(e.recordOrderArrivalRFID(oid,"123456789000")); 
		Integer oid1=e.issueOrder("1234567890128", 1, 1.0);
		e.payOrder(oid1);
		assertFalse(e.recordOrderArrivalRFID(oid1,"123456789000"));
		assertTrue(e.recordOrderArrivalRFID(oid1,"123456789003")); 
	} 
	@Test 
	public void testAddProductToSaleRFID() throws InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException {
		assertThrows(UnauthorizedException.class,()->{
			e.addProductToSaleRFID(1, "1234567890");
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.addProductToSaleRFID(null, "123456789000");
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.addProductToSaleRFID(-1, "123456789000");
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.addProductToSaleRFID(1, null);
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.addProductToSaleRFID(1, "");
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.addProductToSaleRFID(1, "1234");
		});
		assertFalse(e.addProductToSaleRFID(1, "123456789000")); //product non esiste
		
		Integer idp=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		Integer oid=e.issueOrder("1234567890128", 3, 1.0);
		e.updatePosition(idp, "1-a-1");
		e.recordBalanceUpdate(10.0); 
		e.payOrder(oid);
		e.recordOrderArrivalRFID(oid,"123456789000");
		Integer idt=e.startSaleTransaction();
		assertTrue(e.addProductToSaleRFID(idt, "123456789000")); 
		assertFalse(e.addProductToSaleRFID(idt, "123456789000"));
		e.endSaleTransaction(idt);
		assertFalse(e.addProductToSaleRFID(idt, "123456789001"));
		
	}
	@Test
	public void testDeleteProductFromSaleRFID() throws InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException {
		assertThrows(UnauthorizedException.class,()->{
			e.deleteProductFromSaleRFID(1, "123456789000");
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.deleteProductFromSaleRFID(null, "123456789000");
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.deleteProductFromSaleRFID(-1, "123456789000");
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.deleteProductFromSaleRFID(1, null);
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.deleteProductFromSaleRFID(1, "");
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.deleteProductFromSaleRFID(1, "1234");
		}); 
		assertFalse(e.deleteProductFromSaleRFID(1, "123456789000"));
		Integer idp=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		Integer oid=e.issueOrder("1234567890128", 3, 1.0);
		e.updatePosition(idp, "1-a-1");
		e.recordBalanceUpdate(10.0); 
		e.payOrder(oid);
		e.recordOrderArrivalRFID(oid,"123456789000");
		Integer idt=e.startSaleTransaction();
		assertFalse(e.deleteProductFromSaleRFID(idt,"123456789000"));
		e.addProductToSaleRFID(idt, "123456789000");
		assertTrue(e.deleteProductFromSaleRFID(idt, "123456789000"));
		e.addProductToSaleRFID(idt, "123456789001");
		e.endSaleTransaction(idt);
		assertFalse(e.deleteProductFromSaleRFID(idt, "123456789001"));	
		
		
	} 
	//deletesaletransaction
	@Test
	public void testReturnProductRFID() throws InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException {
		assertThrows(UnauthorizedException.class,()->{
			e.returnProductRFID(1, "123456789000");
		});
		e.login("SuperUser", "1234");
		assertThrows(InvalidTransactionIdException.class,()->{
			e.returnProductRFID(null, "123456789000");
		});
		assertThrows(InvalidTransactionIdException.class,()->{
			e.returnProductRFID(-1, "123456789000"); 
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.returnProductRFID(1, null);
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.returnProductRFID(1, "");
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.returnProductRFID(1, "1234");
		});
		assertThrows(InvalidRFIDException.class,()->{
			e.returnProductRFID(1, "1234567890123");
		}); 
		assertFalse(e.returnProductRFID(OperationsCounter.getCounter()+1, "123456789000"));
		Integer idp=e.createProductType("pasta", "1234567890128", 1.0, "cibo");
		Integer oid=e.issueOrder("1234567890128", 3, 1.0);
		e.updatePosition(idp, "1-a-1");
		e.recordBalanceUpdate(10.0); 
		e.payOrder(oid);
		e.recordOrderArrivalRFID(oid,"123456789000");
		Integer ids=e.startSaleTransaction();
		e.addProductToSaleRFID(ids, "123456789000");
		e.endSaleTransaction(ids);
		MySaleTransaction sale = (MySaleTransaction) e.getSaleTransaction(ids);
		sale.setStatus("PAYED");
		Integer idr=e.startReturnTransaction(ids);
		assertFalse(e.returnProductRFID(idr, "123456789004"));
		assertFalse(e.returnProductRFID(idr, "123456789001"));
		assertTrue(e.returnProductRFID(idr, "123456789000"));
		
	}
}
