package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.OperationsCounter;
import it.polito.ezshop.data.PaymentCounter;

public class TestPaymentCounter {
	@Test
	public void testSetCounter() {
		Integer counter=1;
		PaymentCounter c=new PaymentCounter(); 
		PaymentCounter.setCounter(counter);
		assertEquals(counter,PaymentCounter.getCounter());
	}
	@Test
	public void testAddCounter() {
		Integer counter=1; 
		PaymentCounter.setCounter(counter);
		PaymentCounter.add();
		Integer newCounter=counter+1;
		assertEquals(newCounter,PaymentCounter.getCounter());
	}
	@Test
	public void testSubCounter() {
		Integer counter=1;
		PaymentCounter.setCounter(counter);
		PaymentCounter.sub();
		Integer newCounter=counter-1;
		assertEquals(newCounter,PaymentCounter.getCounter());
	}

}
