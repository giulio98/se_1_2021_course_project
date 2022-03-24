package integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

import it.polito.ezshop.data.Employees;
import it.polito.ezshop.data.EmployeesCounter;

public class TestEmployees {
	
	@Test
	public void testAddUser() { 
		Employees e= new Employees();
		Integer newId = e.addUser("paolo", "1234", "Cashier");
		Integer id = EmployeesCounter.getCounter();
		assertEquals(id, newId);
		assertEquals(new Integer(-1),e.addUser("paolo", "1234", "Aministrator"));
	}
	
	@Test
	public void testDeleteUser() { 
		Integer userId=1;
		Employees e= new Employees();
		assertFalse(e.deleteUser(userId));
		Integer userId1=e.addUser(null, "1234", "Cashier"); //1
		assertFalse(e.deleteUser(userId1));
		Integer userId2=e.addUser("", "1234", "Cashier"); //2
		assertFalse(e.deleteUser(userId2));
		Integer userId3=e.addUser("paolo", "1234", "Cashier"); //3
		assertTrue(e.deleteUser(userId3));
	}
	@Test
	public void testGetUser() {
		Employees e = new Employees();
		Integer userId1=e.addUser(null, "1234", "Cashier");
		assertEquals(null,e.getUser(userId1).getUsername());
		Integer userId2=e.addUser("", "1234", "Cashier");
		assertEquals("",e.getUser(userId2).getUsername());
		Integer userId3=e.addUser("paolo", "1234", "Cashier");
		assertEquals("paolo",e.getUser(userId3).getUsername());
	}
	@Test
	public void testGetAllUser() {
		Employees e = new Employees();
		assertTrue(e.getAllUsers().isEmpty());
		e.addUser("paolo", "1234", "Cashier");
		assertFalse(e.getAllUsers().isEmpty());
		assertEquals("paolo",e.getAllUsers().get(0).getUsername());
	} 
	@Test
	public void testSearchUser() {
		Employees e= new Employees();
		assertEquals(null, e.searchUser("paolo", "1234"));
		e.addUser("paolo", "1234", "Cashier");
		assertEquals(null, e.searchUser("paolo", "password"));
		assertEquals("paolo", e.searchUser("paolo", "1234").getUsername());
	}

}
