package integrationTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.Payment;
import it.polito.ezshop.data.CreditCard;

public class TestPayment {
	
	@Test
	public void testComputeChange() {
		//cash
		Payment pch = new Payment(null, 15.00, 20.00, true, 1);
		
		double change = pch.computeChange();
		assertEquals(change, 5.00, 0.001);
	}
	
	@Test
	public void testSetPrice() {
		//credit card
		CreditCard cc = new CreditCard("4485370086510891", 150.00);
		Payment pcc = new Payment(cc, 15.00, 0.0, true, 1);
		
		double price = 25.00;
		pcc.setPrice(price);
		assertEquals(price, pcc.getPrice(), 0.001);
	}
	
	@Test
	public void testSetReturnPrice() {
		//cash
		Payment pch = new Payment(null, 15.00, 20.00, true, 1);
		
		double price = 25.00;
		pch.setReturnPrice(price);
		assertEquals(price, pch.getReturnPrice(), 0.001);
	}
	
	@Test
	public void testSetCashOrCard() {
		//cash
		Payment pch = new Payment(null, 15.00, 20.00, true, 1);
		
		double price = 50.00;
		pch.setCashOrCard(price);
		assertEquals(price, pch.getCashOrCard(), 0.001);
	}
	
	@Test
	public void testSetCommited() {
		//cash
		Payment pch = new Payment(null, 15.00, 20.00, true, 1);
		
		pch.setCommitted(false);
		assertEquals(false, pch.isCommitted());
	}
	
	@Test
	public void testSetPaymentID() {
		//cash
		Payment pch = new Payment(null, 15.00, 20.00, true, 1);
		
		Integer newId = 2;
		pch.setPaymentID(newId);
		assertEquals(newId, pch.getPaymentID());
	}
	
	@Test
	public void testSetCard() {
		///credit card
		CreditCard cc = new CreditCard("4485370086510891", 150.00);
		Payment pcc = new Payment(cc, 15.00, 0.0, true, 1);
		
		CreditCard cc2 = new CreditCard("5100293991053009", 10.00);
		pcc.setCard(cc2);
		assertEquals(cc2, pcc.getCard());
	}
	
}
