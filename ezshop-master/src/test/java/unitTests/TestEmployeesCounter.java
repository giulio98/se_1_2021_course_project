package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.CustomersCounter;
import it.polito.ezshop.data.EmployeesCounter;

public class TestEmployeesCounter {
	@Test
	public void testSetCounter() {
		Integer counter=1;
		EmployeesCounter c= new EmployeesCounter();
		EmployeesCounter.setCounter(counter);
		assertEquals(counter,EmployeesCounter.getCounter());
	}
	@Test
	public void testAddCounter() {
		Integer counter=1;
		EmployeesCounter.setCounter(counter);
		EmployeesCounter.add();
		Integer newCounter=counter+1;
		assertEquals(newCounter,EmployeesCounter.getCounter());
	}
	@Test
	public void testSubCounter() {
		Integer counter=1;
		EmployeesCounter.setCounter(counter);
		EmployeesCounter.sub();
		Integer newCounter=counter-1;
		assertEquals(newCounter,EmployeesCounter.getCounter());
	}
	

}
