package integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

import it.polito.ezshop.data.AccountBook;
import it.polito.ezshop.data.Catalogue;
import it.polito.ezshop.data.MyBalanceOperation;
import it.polito.ezshop.data.MyOrder;
import it.polito.ezshop.data.MyReturnTransaction;
import it.polito.ezshop.data.MySaleTransaction;
import it.polito.ezshop.data.OperationsCounter;

public class TestAccountBook {
	
	@Test
	public void testIssueOrder() {
		AccountBook ac= new AccountBook();
		Integer id=ac.issueOrder("1234567890", 1, 1.0);
		assertEquals(id,OperationsCounter.getCounter());
		MyOrder o=ac.getOrderById(id);
		assertEquals("ISSUED", o.getStatus());
	}
	@Test
	public void testRecoverOrder() {
		AccountBook ac= new AccountBook();
		assertTrue(ac.recoverOrder(1, "1234567890", 1.0, 1, "PAYED"));
		MyOrder o=ac.getOrderById(1);
		assertEquals("PAYED", o.getStatus());
	}
	@Test
	public void testPayOrderFor() {
		AccountBook ac= new AccountBook(); 
		assertEquals(new Integer(-1),ac.payOrderFor("1234567890", 1, 1.0));
		ac.recordBalanceUpdate(5.0, "Credit");
		Integer id=ac.payOrderFor("1234567890", 1, 1.0);
		assertEquals(OperationsCounter.getCounter(),id);
		MyOrder o=ac.getOrderById(id);
		assertEquals("PAYED", o.getStatus()); 
		assertEquals(new Double(4.0),ac.computeBalance());
	}
	@Test
	public void testPayOrder() {
		AccountBook ac= new AccountBook(); 
		Integer id= ac.issueOrder("1234567890", 1, 1.0);
		assertFalse(ac.payOrder(-1));
		MyOrder o = ac.getOrderById(id);
		o.setStatus("PAYED");
		assertFalse(ac.payOrder(id));
		o.setStatus("ORDERED");
		assertFalse(ac.payOrder(id));
		ac.recordBalanceUpdate(5.0, "Credit");
		o.setStatus("ISSUED");
		assertTrue(ac.payOrder(id));
		assertEquals("PAYED",o.getStatus());
	}
	@Test
	public void testRecordOrderArrival() {
		AccountBook ac = new AccountBook();
		assertFalse(ac.recordOrderArrival(-1));
		Integer id= ac.issueOrder("1234567890", 1, 1.0);
		MyOrder o = ac.getOrderById(id);
		o.setStatus("ISSUED");
		assertFalse(ac.recordOrderArrival(id));
		o.setStatus("PAYED");
		assertTrue(ac.recordOrderArrival(id));
		o.setStatus("COMPLETED");
		assertTrue(ac.recordOrderArrival(id));
		assertEquals("COMPLETED",o.getStatus());
	}
	@Test
	public void testGetAllOrders() {
		AccountBook ac = new AccountBook();
		ac.issueOrder("1234567890", 1, 1.0);
		ac.issueOrder("1234567891", 1, 1.0);
		ac.issueOrder("1234567892", 1, 1.0);
		assertEquals(3,ac.getAllOrders().size());
	}
	@Test
	public void testIssueSaleTransaction() {
		AccountBook ac= new AccountBook();
		Integer id=ac.issueSaleTransaction();   
		assertEquals(id,OperationsCounter.getCounter());
		MySaleTransaction st= ac.getSaleTransactionById(id);
		assertEquals("OPENED", st.getStatus());
		
	} 
	@Test 
	public void testAddOrRemoveProductToSale() {
		AccountBook ac= new AccountBook();
		assertFalse(ac.addOrRemoveProductToSale(-1, "1234567890", 2, 0, 1.0, "ciao"));
		Integer id=ac.issueSaleTransaction();
		MySaleTransaction st = ac.getSaleTransactionById(id);
		st.setStatus("PAYED");
		assertFalse(ac.addOrRemoveProductToSale(id, "1234567890", 2, 0, 1.0, "ciao"));
		st.setStatus("OPENED");
		assertTrue(ac.addOrRemoveProductToSale(id, "1234567890",2, 0, 1.0, "ciao"));
		assertTrue(ac.addOrRemoveProductToSale(id, "1234567890",2, 1, 1.0, "ciao"));
	}
	@Test
	public void testRecoverBalanceOperation() {
		AccountBook ac= new AccountBook();
		assertTrue(ac.recoverBalanceOperation(1, "Order", 1.0, LocalDate.now(), "11:00", "Ok"));
	}
	@Test
	public void testAddNewBalanceOperationSale() {
		AccountBook ac= new AccountBook();
		Integer id=ac.issueSaleTransaction();
		MySaleTransaction st= ac.getSaleTransactionById(id);
		MyBalanceOperation bo=ac.addNewBalanceOperationSale(st);
		assertEquals("CLOSED",bo.getStatus());
		
	}
	@Test
	public void testRemoveSaleTransaction() {
		AccountBook ac= new AccountBook();
		Catalogue c = new Catalogue();
		assertFalse(ac.removeSaleTransaction(-1, c));
		Integer id=ac.issueSaleTransaction();
		assertTrue(ac.removeSaleTransaction(id, c));
		assertTrue(ac.getAllSales().isEmpty());
		
	}
	@Test
	public void testRecoverSale() {
		AccountBook ac= new AccountBook();
		assertTrue(ac.recoverSale(1, 0.5, 1.0, 1, "Ok"));
	}
	@Test
	public void testRecoverTicket() {
		AccountBook ac= new AccountBook();
		Integer id=ac.issueSaleTransaction();
		assertTrue(ac.recoverTicket(id, "1234567890", "ciao", 2, 1.0, 2.0, 0.5, 1));
	}
	@Test
	public void testIssueReturnTransaction() {
		AccountBook ac = new AccountBook();
		Integer idt=ac.issueSaleTransaction();
		MySaleTransaction st= ac.getSaleTransactionById(idt);
		Integer idr=ac.issueReturnTransaction(st);   
		assertEquals(idr,OperationsCounter.getCounter());
		MyReturnTransaction rt= ac.getReturnTransactionById(idr);
		assertEquals("OPENED", rt.getStatus());
		
	}
	@Test
	public void testRemoveReturnTransaction() {
		AccountBook ac = new AccountBook();
		assertFalse(ac.removeReturnTransaction(-1));
		Integer idt=ac.issueSaleTransaction();
		MySaleTransaction st= ac.getSaleTransactionById(idt);
		Integer idr=ac.issueReturnTransaction(st);   
		assertTrue(ac.removeReturnTransaction(idr));
		assertEquals(null,ac.getReturnTransactionById(idr));
		
	}
	@Test
	public void testAddNewBalanceOperationReturn() {
		AccountBook ac= new AccountBook();
		Integer id=ac.issueSaleTransaction();
		MySaleTransaction st= ac.getSaleTransactionById(id);
		Integer idr=ac.issueReturnTransaction(st);
		MyReturnTransaction rt= ac.getReturnTransactionById(idr);
		MyBalanceOperation bo=ac.addNewBalanceOperationReturn(rt);
		assertEquals("CLOSED",bo.getStatus());
		
	}
	@Test
	public void testRecordBalanceUpdate() {
		AccountBook ac= new AccountBook();
		ac.recordBalanceUpdate(5.0, "Credit");
		ac.recordBalanceUpdate(-1.0, "Debit");
		ac.recordBalanceUpdate(-1.0, "Order"); 
		Integer idt=ac.issueSaleTransaction();
		MySaleTransaction st= ac.getSaleTransactionById(idt);
		MyBalanceOperation bo=ac.addNewBalanceOperationSale(st);
		bo.setStatus("PAYED");
		st.setStatus("PAYED");
		ac.recordBalanceUpdate(1.0, "Sale");
		bo.setMoney(1.0); 
		assertEquals(new Double(4.0),ac.computeBalance());
	}
	@Test
	public void testRemoveBalanceOperation() {
		AccountBook ac= new AccountBook(); 
		assertFalse(ac.removeBalanceOperation(-1));
		Integer idt=ac.issueSaleTransaction();
		MySaleTransaction st= ac.getSaleTransactionById(idt);
		MyBalanceOperation bo=ac.addNewBalanceOperationSale(st);
		assertEquals(bo,ac.searchBalanceOperation(idt));
		assertTrue(ac.removeBalanceOperation(idt));
		
	} 
	@Test
	public void testGetBalanceOperationByDate() {
		AccountBook ac= new AccountBook();
		assertTrue(ac.getBalanceOperationByDate(null, null).isEmpty());
		Integer idt1=ac.issueSaleTransaction();
		MySaleTransaction st1= ac.getSaleTransactionById(idt1);
		MyBalanceOperation bo1=ac.addNewBalanceOperationSale(st1);
		bo1.setDate(LocalDate.of(2021, Month.MAY, 23));
		Integer idt2=ac.issueSaleTransaction();
		MySaleTransaction st2= ac.getSaleTransactionById(idt2);
		MyBalanceOperation bo2=ac.addNewBalanceOperationSale(st2);
		bo2.setDate(LocalDate.of(2021, Month.MAY, 25));
		Integer idt3=ac.issueSaleTransaction();
		MySaleTransaction st3= ac.getSaleTransactionById(idt3);
		MyBalanceOperation bo3=ac.addNewBalanceOperationSale(st3);
		bo3.setDate(LocalDate.of(2021, Month.MAY, 24));
		Integer idt4=ac.issueSaleTransaction();
		MySaleTransaction st4= ac.getSaleTransactionById(idt4);
		MyBalanceOperation bo4=ac.addNewBalanceOperationSale(st4);
		bo4.setDate(LocalDate.of(2021, Month.MAY, 22));
		Integer idt5=ac.issueSaleTransaction();
		MySaleTransaction st5= ac.getSaleTransactionById(idt5);
		MyBalanceOperation bo5=ac.addNewBalanceOperationSale(st5);
		bo5.setDate(LocalDate.of(2021, Month.MAY, 26));
		ac.getBalanceOperationByDate(null, null);
		ac.getBalanceOperationByDate(null, LocalDate.of(2021, Month.MAY, 25));
		ac.getBalanceOperationByDate(LocalDate.of(2021, Month.MAY, 23), null);
		ac.getBalanceOperationByDate(LocalDate.of(2021, Month.MAY, 23), LocalDate.of(2021, Month.MAY, 25));
		ac.getBalanceOperationByDate(null, LocalDate.of(2021, Month.MAY, 26));
	}
	
	

}
