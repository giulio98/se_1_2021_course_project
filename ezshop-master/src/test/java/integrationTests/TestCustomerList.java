package integrationTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.CustomerList;
import it.polito.ezshop.data.CustomersCounter;
import it.polito.ezshop.view.MyCustomer;

public class TestCustomerList {
	
	@Before
	public void resetBeforeEach() {
		CustomersCounter.setCounter(0);
	}
	
	@Test
	public void testAddCustomer() {
		CustomerList cl = new CustomerList();
		String str = new String("prova");
		Integer id1 = cl.addCustomer(str);
		Integer id2 = cl.addCustomer(str);
		Integer ass2 = -1;
		Integer ass1 = 1;
		assertEquals(ass1,id1); 
		assertEquals(ass2,id2);  
	}
	
	@Test
	public void testGetCustomer() {
		String str = new String("prova");
		CustomerList cl = new CustomerList();
		Integer id = cl.addCustomer(str); 
		assertEquals(cl.getCustomer(id).getCustomerName(), str); 
	}
	
	@Test
	public void testGetCustomerName() {
		String str = new String("prova");
		CustomerList cl = new CustomerList();
		Integer id = cl.addCustomer(str); 
		assertEquals(cl.getCustomerName(id), str); 
		assertEquals("",cl.getCustomerName(-1));
	}

	@Test
	public void testGetAllCustomer() {
		String str1 = new String("prova1");
		String str2 = new String("prova2");
		CustomerList cl = new CustomerList();
		List<Customer> lista = cl.getAllCustomer();
		assertEquals(lista.size(), 0); 
		Integer id1 = cl.addCustomer(str1); 
		Integer id2 = cl.addCustomer(str2); 
		List<Customer> list = cl.getAllCustomer();
		assertNotNull(list);
		assertEquals(list.size(), 2); 
	}
	
	@Test
	public void testDeleteCustomer() {
		String str = new String("prova");
		CustomerList cl = new CustomerList();
		Integer id = cl.addCustomer(str);  
		assertFalse(cl.deleteCustomer(-1));
		cl.deleteCustomer(id);
		assertEquals(0, cl.getAllCustomer().size());
		Integer id2= cl.addCustomer(null);
		assertFalse(cl.deleteCustomer(id2));
		Integer id3= cl.addCustomer("");
		assertFalse(cl.deleteCustomer(id3));
		
	}
	
	@Test
	public void testSearchCustomer() {
		String str = new String("prova");
		CustomerList cl = new CustomerList();
		Integer id = cl.addCustomer(str); 
		it.polito.ezshop.data.MyCustomer cust;
		cust = cl.searchCustomer("");
		assertNull(cust);
		cust = cl.searchCustomer("nonaggiunto");
		assertNull(cust);
		cust = cl.searchCustomer(str);
		assertNotNull(cust);
		assertEquals(cust.getCustomerName(), str); 
	}
	
	@Test
	public void testModifyCustomer() {
		String str = new String("prova");
		CustomerList cl = new CustomerList();
		Integer id = cl.addCustomer(str); 
		it.polito.ezshop.data.MyCustomer cust;
		assertFalse(cl.modifyCustomer("",""));
		assertFalse(cl.modifyCustomer(null,""));
		assertFalse(cl.modifyCustomer("nonaggiunto",""));
		assertTrue(cl.modifyCustomer("prova","123"));
	}
	
	@Test
	public void modifyCustomerName() {
		String str = new String("prova");
		CustomerList cl = new CustomerList();
		Integer id = cl.addCustomer(str); 
		assertFalse(cl.modifyCustomerName(id,""));
		assertFalse(cl.modifyCustomerName(id,null));
		assertFalse(cl.modifyCustomerName(-1,"prova"));
		assertTrue(cl.modifyCustomerName(id,"antonio"));
		assertEquals(cl.getCustomerName(id),"antonio");
	}
	
	@Test
	public void testSearchCardOwner() {
		String str = new String("prova");
		CustomerList cl = new CustomerList();
		Integer id = cl.addCustomer(str); 
		cl.modifyCustomer("prova","123");
		Integer id2= cl.addCustomer("gino");
		Integer id3 = cl.addCustomer("paolo");
		cl.modifyCustomer("paolo", "321");
		assertEquals(cl.searchCardOwner(""),"");
		assertEquals(cl.searchCardOwner(null),"");
		assertEquals(cl.searchCardOwner("123"),"prova");
		cl.deleteCustomer(id);
		cl.deleteCustomer(id2);
		cl.deleteCustomer(id3);
		assertEquals("",cl.searchCardOwner("123"));
	}
	
	@Test
	public void testUpdatePoints() {
		String str = new String("prova");
		CustomerList cl = new CustomerList();
		Integer id = cl.addCustomer(str); 
		cl.modifyCustomer("prova","123");
		assertFalse(cl.updatePoints(null,0));
		assertFalse(cl.updatePoints("",0));
		assertFalse(cl.updatePoints("222",0));
		assertTrue(cl.updatePoints("123",3));
		assertEquals(cl.getCustomer(id).getPoints(), (Integer)3); 
		cl.addCustomer(null);
		cl.modifyCustomer(null, "223");
		cl.modifyCustomer("", "224");
		assertFalse(cl.updatePoints("223", 0));
		assertFalse(cl.updatePoints("224", 0));
		
	}
	
}
