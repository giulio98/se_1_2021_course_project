package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.TicketCounter;

public class TestTicketCounter {
	@Test
	public void testSetCounter() {
		Integer counter=1;
		TicketCounter c= new TicketCounter();
		TicketCounter.setCounter(counter);
		assertEquals(counter,TicketCounter.getCounter());
	}
	@Test
	public void testAddCounter() {
		Integer counter=1;
		TicketCounter.setCounter(counter);
		TicketCounter.add();
		Integer newCounter=counter+1;
		assertEquals(newCounter,TicketCounter.getCounter());
	}
	@Test
	public void testSubCounter() {
		Integer counter=1;
		TicketCounter.setCounter(counter);
		TicketCounter.sub();
		Integer newCounter=counter-1;
		assertEquals(newCounter,TicketCounter.getCounter());
	}
	

}
