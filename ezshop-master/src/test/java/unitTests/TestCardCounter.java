package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.CardCounter;

public class TestCardCounter {
	@Test
	public void testSetCounter() {
		String counter="1"; 
		CardCounter c= new CardCounter(); 
		CardCounter.setCounter(counter);
		assertEquals(counter,CardCounter.getCounter());
	}
	@Test
	public void testAddCounter() {
		String counter="1000000000"; 
		CardCounter.setCounter(counter);
		CardCounter.add();
		Long newCounter=Long.parseLong(counter)+1;
		assertEquals(newCounter.toString(),CardCounter.getCounter());
	}
	@Test
	public void testSubCounter() {
		String counter="1"; 
		CardCounter.setCounter(counter);
		CardCounter.sub();
		Integer newCounter=Integer.parseInt(counter)-1;
		assertEquals(newCounter.toString(),CardCounter.getCounter());
	}
	

}
