package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.MyCustomer;

public class TestMyCustomer {
	@Test
	public void testSetCustomerName() {
		String name="paolo";
		MyCustomer c= new MyCustomer(2,"giorgio");
		c.setCustomerName(name);
		assertEquals(name,c.getCustomerName());
	}
	@Test
	public void testSetCustomerCard() {
		String card="1234567890";
		MyCustomer c= new MyCustomer(2,"giorgio");
		c.setCustomerCard(card);
		assertEquals(card,c.getCustomerCard());
	}
	@Test
	public void testSetCustomerId() {
		Integer id=1;
		MyCustomer c= new MyCustomer(2,"giorgio");
		c.setId(id);
		assertEquals(id,c.getId());
	}
	@Test 
	public void testSetPoints() {
		Integer points=12;
		MyCustomer c= new MyCustomer(2,"giorgio");
		c.setPoints(points);
		assertEquals(points,c.getPoints());
	}
	@Test
	public void testAddPoints() {
		Integer prevPoints=12;
		Integer points=12;
		MyCustomer c= new MyCustomer(2,"giorgio");
		c.setPoints(prevPoints);
		c.addPoints(points);
		Integer tot=prevPoints+points;
		assertEquals(tot,c.getPoints());
	}

}
