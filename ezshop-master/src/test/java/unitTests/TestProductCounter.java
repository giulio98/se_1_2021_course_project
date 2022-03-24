package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.PaymentCounter;
import it.polito.ezshop.data.ProductCounter;

public class TestProductCounter {
	@Test
	public void testSetCounter() {
		Integer counter=1;
		ProductCounter c= new ProductCounter();
		ProductCounter.setCounter(counter);
		assertEquals(counter,ProductCounter.getCounter());
	}
	@Test
	public void testAddCounter() {
		Integer counter=1;
		ProductCounter.setCounter(counter);
		ProductCounter.add();
		Integer newCounter=counter+1;
		assertEquals(newCounter,ProductCounter.getCounter());
	}
	@Test
	public void testSubCounter() {
		Integer counter=1;
		ProductCounter.setCounter(counter);
		ProductCounter.sub();
		Integer newCounter=counter-1;
		assertEquals(newCounter,ProductCounter.getCounter());
	}
	

}
