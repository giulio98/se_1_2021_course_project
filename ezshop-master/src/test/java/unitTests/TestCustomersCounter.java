package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.CustomersCounter;

public class TestCustomersCounter {
	@Test
	public void testSetCounter() {
		Integer counter=1;
		CustomersCounter c= new CustomersCounter();
		CustomersCounter.setCounter(counter);
		assertEquals(counter,CustomersCounter.getCounter());
	}
	@Test
	public void testAddCounter() {
		Integer counter=1;
		CustomersCounter.setCounter(counter);
		CustomersCounter.add();
		Integer newCounter=counter+1;
		assertEquals(newCounter,CustomersCounter.getCounter());
	}
	@Test
	public void testSubCounter() {
		Integer counter=1;
		CustomersCounter.setCounter(counter);
		CustomersCounter.sub();
		Integer newCounter=counter-1;
		assertEquals(newCounter,CustomersCounter.getCounter());
	}
	
} 
