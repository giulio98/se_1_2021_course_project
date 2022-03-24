package integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.Catalogue;
import it.polito.ezshop.data.MyProductType;
import it.polito.ezshop.data.MySaleTransaction;
import it.polito.ezshop.data.MyTicketEntry;
import it.polito.ezshop.data.TicketEntry;

public class TestMySaleTransaction {

	@Test
	public void testSetTicketNumber() {
		MySaleTransaction sale = new MySaleTransaction(1);
		
		Integer newId = 2; 
		sale.setTicketNumber(newId);
		
		assertEquals(newId, sale.getTicketNumber());
	}
	
	@Test
	public void testSetEntries() {
		MySaleTransaction sale = new MySaleTransaction(1);
		
		assertEquals(Collections.emptyList(), sale.getEntries());
		
		List<TicketEntry> entries = new ArrayList<TicketEntry>();
		MyTicketEntry te1 = new MyTicketEntry("123456789128", 10, 1.00, "milk");
		entries.add(te1);
		MyTicketEntry te2 = new MyTicketEntry("1234567891231", 5, 2.00, "coca-cola");
		entries.add(te2);
		
		sale.setEntries(entries);
		assertNotEquals(Collections.emptyList(), sale.getEntries());
		
		assertNotNull(sale.getTicketEntry("1234567891231"));
		assertNull(sale.getTicketEntry("12345678912343"));
	}
	
	@Test
	public void testSetDiscountRate() {
		MySaleTransaction sale = new MySaleTransaction(1);
		
		double discount = 0.2;
		sale.setDiscountRate(discount);
		
		assertEquals(discount, sale.getDiscountRate(), 0.001);
	}
	
	@Test
	public void testSetPrice() {
		MySaleTransaction sale = new MySaleTransaction(1);
		
		double price = 20.00;
		sale.setPrice(price);
		
		assertEquals(price, sale.getPrice(), 0.001);
	}
	
	@Test
	public void testSetStatus() {
		MySaleTransaction sale = new MySaleTransaction(1);
		
		String status = "OPENED";
		sale.setStatus(status);
		assertEquals(status, sale.getStatus());
		
		status = "CLOSED";
		sale.setStatus(status);
		assertEquals(status, sale.getStatus());
		
		status = "PAYED";
		sale.setStatus(status);
		assertEquals(status, sale.getStatus());
	}
	
	@Test
	public void testAddProductToSale() {
		MySaleTransaction sale = new MySaleTransaction(1);
		
		assertEquals(Collections.emptyList(), sale.getEntries());
		
		sale.addProductToSale("123456789128", 1, 1.00, "milk");
		assertNotEquals(Collections.emptyList(), sale.getEntries());
		
		sale.addProductToSale("123456789128", 2, 1.00, "milk");
		assertEquals(1, sale.getEntries().size());
		
		sale.addProductToSale("1234567891231", 2, 2.00, "coca-cola");
		assertEquals(2, sale.getEntries().size());
		
		sale.setStatus("CLOSED");
		assertFalse(sale.addProductToSale("1234567891231", 2, 2.00, "coca-cola"));
	}
	
	@Test
	public void testRemoveProductFromSale() {
		MySaleTransaction sale = new MySaleTransaction(1);
		
		assertEquals(Collections.emptyList(), sale.getEntries());
		
		sale.addProductToSale("123456789128", 1, 1.00, "milk");
		sale.addProductToSale("1234567891231", 2, 2.00, "coca-cola");
		
		sale.removeProductFromSale("1234567891231", 1);
		assertEquals(2, sale.getEntries().size());
		
		sale.removeProductFromSale("1234567891231", 1); 
		//assertEquals(1, sale.getEntries().size()); cambiamento: la entry non viene più eliminata
		
		assertFalse(sale.removeProductFromSale("1234567891231", 1));
		assertFalse(sale.removeProductFromSale("123456789128", 2));
	}
	
	@Test
	public void testComputeTotalPrice() {
		MySaleTransaction sale = new MySaleTransaction(1);
		
		sale.addProductToSale("123456789128", 1, 1.00, "milk");
		sale.addProductToSale("1234567891231", 2, 2.00, "coca-cola");
		
		assertEquals(5.00, sale.computeTotalPrice(), 0.001);
		
		sale.addProductToSale("1234567891231", 3, 2.00, "coca-cola");
		
		assertEquals(11.00, sale.computeTotalPrice(), 0.001);
	}
	
	@Test
	public void testAddDiscountToProduct() {
		MySaleTransaction sale = new MySaleTransaction(1);
		
		sale.addProductToSale("123456789128", 1, 1.00, "milk");
		sale.addProductToSale("1234567891231", 2, 2.00, "coca-cola");
		
		double discount = 0.1;
		assertTrue(sale.addDiscountToProduct("1234567891231", discount));
		assertEquals(4.60, sale.getPrice(), 0.001);
		
		assertFalse(sale.addDiscountToProduct("12345678912343", discount));
		assertEquals(4.60, sale.getPrice(), 0.001);
	}
	
	@Test
	public void testComputePointsEarned() {
		MySaleTransaction sale = new MySaleTransaction(1);
				
		sale.addProductToSale("123456789128", 1, 1.00, "milk");
		sale.addProductToSale("1234567891231", 5, 2.00, "coca-cola");
		
		sale.computePointsEarned();
		assertEquals(1, sale.getPointsEarned());
		
		double price = 11.00;
		sale.computePointsEarned(price);
		assertEquals(1, sale.getPointsEarned());
		
		sale.addProductToSale("12345678912343", 5, 4.00, "parmigiano");
		assertEquals(3, sale.getPointsEarned());
	}
	
	@Test
	public void testRemoveAllProductFromSale() {
		Catalogue cat = new Catalogue();
		cat.addProductType("milk", "123456789128", 1.00, "");
		cat.addProductType("coca-cola", "1234567891231", 2.00, "");
		
		MyProductType pt1 = (MyProductType) cat.getProductTypeByBarCode("123456789128");
		MyProductType pt2 = (MyProductType) cat.getProductTypeByBarCode("1234567891231");
		pt1.updatePosition("1-a-1");
		pt2.updatePosition("2-a-2");
		pt1.updateQuantity(20);
		pt2.updateQuantity(20);
		
		assertEquals(new Integer(20), pt1.getQuantity());
		assertEquals(new Integer(20), pt2.getQuantity());
		
		MySaleTransaction sale = new MySaleTransaction(1);
		
		sale.addProductToSale("123456789128", 1, 1.00, "milk");
		sale.addProductToSale("1234567891231", 2, 2.00, "coca-cola");
		
		pt1.updateQuantity(-1);
		pt2.updateQuantity(-2);
		
		assertEquals(new Integer(19), pt1.getQuantity()); //NON FUNZIONA PERCHE NON VENGONO AGGIORNATE QUI LE QUANTITA
		assertEquals(new Integer(18), pt2.getQuantity());
		
		sale.removeAllProductsFromSale(cat);
		assertEquals(new Integer(20), pt1.getQuantity());
		assertEquals(new Integer(20), pt2.getQuantity());
	}
	
	@Test
	public void testSetDbUpdating() {
		MySaleTransaction sale = new MySaleTransaction(1);

		sale.setDbUpdating(true);
		assertTrue(sale.getDbUpdating());
	}
	
	@Test
	public void testSetPointsEarned() {
		MySaleTransaction sale = new MySaleTransaction(1);

		sale.setPointsEarned(4);
		assertEquals(4, sale.getPoints());
	}
	
}
