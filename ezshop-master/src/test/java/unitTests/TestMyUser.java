package unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.data.MyUser;

public class TestMyUser {
	@Test
	public void testSetUserId() {
		Integer id=1;
		MyUser u= new MyUser(2,"Cashier","giorgio","1234");
		u.setId(id);
		assertEquals(id,u.getId());
	}
	@Test 
	public void testSetUsername() {
		String username="paolo";
		MyUser u= new MyUser(2,"Cashier","giorgio","1234");
		u.setUsername(username);
		assertEquals(username,u.getUsername());
	}
	@Test
	public void testSetPassword() {
		String password="password";
		MyUser u= new MyUser(2,"Cashier","giorgio","1234");
		u.setPassword(password);
		assertEquals(password,u.getPassword());
	}
	@Test
	public void testSetRole() {
		String role="Administrator";
		MyUser u= new MyUser(2,"Cashier","giorgio","1234");
		u.setRole(role);
		assertEquals(role,u.getRole());
	}
	@Test
	public void testVerifyAutentication() {
		String password="password";
		MyUser u= new MyUser(2,"Cashier","giorgio","password");
		assertTrue(u.verifyAutentication(password));
		assertFalse(u.verifyAutentication("1234"));
	}
}
