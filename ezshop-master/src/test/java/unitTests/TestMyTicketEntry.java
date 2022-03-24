package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.MyTicketEntry;

public class TestMyTicketEntry {
	@Test 
	public void testSetBarcode() {
		String barcode="1234567890";
		MyTicketEntry t= new MyTicketEntry("1234567891",2,1.0,"cereali");
		t.setBarCode(barcode);
		assertEquals(barcode,t.getBarCode());
	}
	@Test
	public void testSetProductDescprition() {
		String description="pasta";
		MyTicketEntry t= new MyTicketEntry("1234567891",2,1.0,"cereali");
		t.setProductDescription(description);
		assertEquals(description,t.getProductDescription());
	}
	@Test
	public void testSetAmount() { 
		int amount=1;
		MyTicketEntry t= new MyTicketEntry("1234567891",2,1.0,"cereali");
		t.setAmount(amount);
		assertEquals(amount,t.getAmount());
	}
	
	/*
	@Test
	public void testAddAmount() {
		int prevAmount=1;
		int amount=2;
		MyTicketEntry t= new MyTicketEntry("1234567891",2,1.0,"cereali");
		t.setAmount(prevAmount);
		t.addAmount(amount);
		int tot=prevAmount+amount;
		assertEquals(tot,t.getAmount());
	}*/
	@Test
	public void testSetPricePerUnit() {
		double price=12;
		MyTicketEntry t= new MyTicketEntry("1234567891",13,1.0,"cereali");
		t.setPricePerUnit(price);
		assertEquals(price,t.getPricePerUnit(),0);
	}
	@Test
	public void testSetDiscountRate() {
		double discount=0.5;
		MyTicketEntry t= new MyTicketEntry("1234567891",13,0.6,"cereali");
		t.setDiscountRate(discount);
		assertEquals(discount,t.getDiscountRate(),0);
	}
	@Test
	public void testSetAmountReturned() {
		int amountReturned=1;
		MyTicketEntry t= new MyTicketEntry("1234567891",13,1.0,"cereali");
		t.setAmountReturned(amountReturned);
		assertEquals(amountReturned,t.getAmountReturned());
	}
	@Test
	public void testUpdateAmountReturned() {
		int prevAmount=12;
		int amount=1;
		MyTicketEntry t= new MyTicketEntry("1234567891",13,1.0,"cereali");
		t.setAmountReturned(prevAmount);
		t.updateAmountReturned(amount); 
		assertEquals(prevAmount+amount,t.getAmountReturned()); 
	}
	
	@Test
	public void testComputeTotal() {
		MyTicketEntry t= new MyTicketEntry("1234567891",12,1.0,"cereali");
		t.setDiscountRate(0.5);
		assertEquals(6,t.getTotal(),0);
	}

}
