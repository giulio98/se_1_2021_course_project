package unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.data.CreditCard;

public class TestCreditCard {
	@Test
	public void testSetCreditCardNumber() {
		String card="1234567890";
		CreditCard c= new CreditCard("1234567891",600);
		c.setCreditCardNumber(card);
		assertEquals(card,c.getCreditCardNumber());
	}
	@Test
	public void testSetBalance() {
		double balance=500;
		CreditCard c= new CreditCard("1234567891",600);
		c.setBalance(balance);
		assertEquals(balance,c.getBalance(),0);

    }
	@Test
	public void testCheckBalance() {
		double lowPrice=400; 
		double highPrice=600;
		double boundaryPrice=500;
		CreditCard c= new CreditCard("1234567891",500);
		assertTrue(c.checkBalance(lowPrice));
		assertTrue(c.checkBalance(boundaryPrice));
		assertFalse(c.checkBalance(highPrice)); 
	}
	@Test
	public void testCheckCreditCardLunh() {  
		CreditCard c= new CreditCard("1234567891",500);
		assertFalse(c.checkCreditCardLunh());
		c.setCreditCardNumber(""); //0 iterations 
		assertFalse(c.checkCreditCardLunh());
		c.setCreditCardNumber(null); //0 iterations 
		assertFalse(c.checkCreditCardLunh());
		c.setCreditCardNumber("1"); //1 iteration
		assertFalse(c.checkCreditCardLunh());
		c.setCreditCardNumber("4485370086510891"); //>=2 iteration
		assertTrue(c.checkCreditCardLunh());
	}
}
