package integrationTests;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.data.CreditCard;
import it.polito.ezshop.data.MyBalanceOperation;
import it.polito.ezshop.data.Payment;

public class TestMyBalanceOperation {
	
	@Test
	public void testSetBalanceId() {
		MyBalanceOperation bo = new MyBalanceOperation(1, "Credit");
		
		int newId = 2;
		bo.setBalanceId(newId);
		
		assertEquals(newId, bo.getBalanceId());
	}
	
	@Test
	public void testSetDate() {
		MyBalanceOperation bo = new MyBalanceOperation(1, "Sale");
		
		LocalDate date = LocalDate.of(2021, 6, 22);
		bo.setDate(date);
		
		assertEquals(date, bo.getDate());
	}
	
	@Test
	public void testSetMoney() {
		MyBalanceOperation bo = new MyBalanceOperation(1, "Return");
		
		double money = 20.00;
		bo.setMoney(money);
		
		assertEquals(money, bo.getMoney(), 0.001);
	}
	
	@Test
	public void testSetType() {
		MyBalanceOperation bo = new MyBalanceOperation(1, "Credit");
		
		String type = "Debit";
		bo.setType(type);
		
		assertEquals(type, bo.getType());
	}
	
	@Test
	public void testSetTime() {
		MyBalanceOperation bo = new MyBalanceOperation(1, "Credit");
		
		String time = "13:22:30";
		bo.setTime(time);
		
		assertEquals(time, bo.getTime());
	}
	
	@Test
	public void testSetStatus() {
		MyBalanceOperation bo = new MyBalanceOperation(1, "Credit");
		
		String status = "CLOSED";
		bo.setStatus(status);
		
		assertEquals(status, bo.getStatus());
	}
	
	@Test
	public void testSetPayment() {
		MyBalanceOperation bo = new MyBalanceOperation(1, "Credit");
		Payment pch = new Payment(null, 15.00, 20.00, true, 1);
		
		bo.setPayment(pch);
		
		assertEquals(pch, bo.getPayment());
		
		double price = 18.00;
		bo.setMoney(price);
		assertEquals(price, pch.getPrice(), 0.001);
	}
	
	@Test
	public void testStartPaymentCard() {
		MyBalanceOperation bo = new MyBalanceOperation(1, "Credit");
		CreditCard cc = new CreditCard("4485370086510891", 150.00);
		
		double price = 20.00;
		bo.startPayment(cc, price);
		
		assertEquals(price, bo.getTotalPrice(), 0.001);
	}
	
	@Test
	public void testStartPaymentCash() {
		MyBalanceOperation bo = new MyBalanceOperation(1, "Credit");
		
		double price = 20.00;
		double cash = 30.00;
		bo.startPayment(price, cash);
		
		assertEquals(price, bo.getTotalPrice(), 0.001);
		assertEquals(cash-price, bo.getReturnValue(), 0.001);
	}
	
}
