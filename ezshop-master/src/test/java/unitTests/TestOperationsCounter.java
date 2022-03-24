package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.EmployeesCounter;
import it.polito.ezshop.data.OperationsCounter;

public class TestOperationsCounter {
	@Test
	public void testSetCounter() {
		Integer counter=1;
		OperationsCounter c= new OperationsCounter();
		OperationsCounter.setCounter(counter);
		assertEquals(counter,OperationsCounter.getCounter());
	}
	@Test
	public void testAddCounter() {
		Integer counter=1;
		OperationsCounter.setCounter(counter);
		OperationsCounter.add();
		Integer newCounter=counter+1;
		assertEquals(newCounter,OperationsCounter.getCounter());
	}
	@Test
	public void testSubCounter() {
		Integer counter=1;
		OperationsCounter.setCounter(counter);
		OperationsCounter.sub();
		Integer newCounter=counter-1;
		assertEquals(newCounter,OperationsCounter.getCounter());
	}
	

}
