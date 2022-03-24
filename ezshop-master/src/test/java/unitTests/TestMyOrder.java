package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.MyOrder;

public class TestMyOrder {
	
	@Test
	public void testSetBalanceId() {
		Integer balanceId=1;
		MyOrder o= new MyOrder(1,"1234567890",2,1.0);
		o.setBalanceId(balanceId);
		assertEquals(balanceId,o.getBalanceId());
	}
	@Test
	public void testSetProductCode() {
		String productCode="1234567890";
		MyOrder o= new MyOrder(1,"1234567891",2,1.0);
		o.setProductCode(productCode);
		assertEquals(productCode,o.getProductCode());
	}
	@Test
	public void testSetPricePerUnit() {
		double pricePerUnit=12.0;
		MyOrder o= new MyOrder(1,"1234567891",2,1.0);
		o.setPricePerUnit(pricePerUnit);
		assertEquals(pricePerUnit,o.getPricePerUnit(),0);
	}
	@Test
	public void testSetQuantity() {
		int quantity=12;
		MyOrder o= new MyOrder(1,"1234567891",2,1.0);
		o.setQuantity(quantity);
		assertEquals(quantity,o.getQuantity());
	}
	@Test
	public void testSetStatus() {
		String status="payed";
		MyOrder o= new MyOrder(1,"1234567891",2,1.0);
		o.setStatus(status);
		assertEquals(status,o.getStatus());
	}
	@Test
	public void testSetOrderId() {
		Integer orderId=1;
		MyOrder o= new MyOrder(1,"1234567890",2,1.0);
		o.setOrderId(orderId);
		assertEquals(orderId,o.getOrderId());
	}

}
