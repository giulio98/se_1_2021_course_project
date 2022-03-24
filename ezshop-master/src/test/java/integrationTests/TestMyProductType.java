package integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import it.polito.ezshop.data.MyProductType;
import it.polito.ezshop.data.Position;

public class TestMyProductType {
	@Test
	public void testSetQuantity() {
		Integer quantity = 10;
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", 1.00, "");
		mpt.setQuantity(quantity);
		assertEquals(quantity, mpt.getQuantity());
	}
	
	@Test 
	public void testSetLocation() {
		String location = "1-a-1";
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", 1.00, "");
		
		mpt.setLocation(location);
		assertEquals(location, mpt.getLocation());
		
		location = "";
		mpt.setLocation(location);
		assertEquals(location, mpt.getLocation());
		
		location = null;
		mpt.setLocation(location);
		assertEquals("", mpt.getLocation());
	}
	
	@Test
	public void testSetNote() {
		String note = "fresh";
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", 1.00, note);
		mpt.setNote(note);
		assertEquals(note, mpt.getNote());
		
		note = "";
		mpt.setNote(note);
		assertEquals(note, mpt.getNote());
		
		note = null;
		mpt.setNote(note);
		assertEquals("", mpt.getNote());
		
		note = null;
		mpt.setNote(note);
		assertEquals("", mpt.getNote());
		
		MyProductType mpt1 = new MyProductType(1, "123456789128", "milk", 1.00, null);
		assertEquals("", mpt.getNote());
	}
	
	@Test
	public void testSetProductDescription() {
		String description = "coca-cola";
		MyProductType mpt = new MyProductType(1, "123456789128", description, 1.00, "");
		description = "coca-cola zero";
		mpt.setProductDescription(description);
		assertEquals(description, mpt.getProductDescription());
	}
	
	@Test
	public void testSetBarCode() {
		String barCode = "123456789128";
		MyProductType mpt = new MyProductType(1, barCode, "milk", 1.00, "");
		barCode = "1234567891231";
		mpt.setBarCode(barCode);
		assertEquals(barCode, mpt.getBarCode());
	}
	
	@Test
	public void testSetPricePerUnit() {
		Double price = 2.00;
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", price, "");
		price = 1.50;
		mpt.setPricePerUnit(price);
		assertEquals(price, mpt.getPricePerUnit());
	}
	
	@Test
	public void testSetId() {
		Integer id = 1;
		MyProductType mpt = new MyProductType(id, "123456789128", "milk", 1.00, "");
		id = 2;
		mpt.setId(id);
		assertEquals(id, mpt.getId());
	}
	
	@Test
	public void testUpdateQuantity() {
		Integer quantity = 10;
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", 1.00, "");
		mpt.setQuantity(quantity);
		
		assertFalse(mpt.updateQuantity(-20));
		assertFalse(mpt.updateQuantity(-11));
		assertTrue(mpt.updateQuantity(10));
		assertTrue(mpt.updateQuantity(-20));
	}
	
	@Test
	public void testUpdatePosition() {
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", 1.00, "");
		
		String newPos = null;
		mpt.updatePosition(newPos);
		assertEquals("", mpt.getLocation());
		
		String location = "1-a-1";
		mpt.setLocation(location);
		
		mpt.updatePosition("");
		assertEquals("", mpt.getLocation());
		
		String newLocation = "2-c-4";
		mpt.updatePosition(newLocation);
		assertEquals(newLocation, mpt.getLocation());
		
	} 
	@Test
	public void testAddRFID() {
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", 1.00, "");
		String RFID= "123456789000";
		assertFalse(mpt.RFIDisPresent(RFID));
		mpt.addRFID(RFID);
		assertTrue(mpt.RFIDisPresent(RFID));
		String RFID1 ="12345678000";
		mpt.addRFID(RFID1);
		assertTrue(mpt.RFIDisPresent(RFID1));
		
	} 
	@Test
	public void addToSoldRFID() {
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", 1.00, "");
		String RFID= "123456789000";
		assertFalse(mpt.rfidIsSold(RFID));
		Integer transactionId= 1;
		mpt.addToSoldRfids(RFID, transactionId);
		assertTrue(mpt.rfidIsSold(RFID));
		
		
	}
	@Test
	public void testRemoveRFIDFromSold() {
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", 1.00, "");
		String RFID= "123456789000";
		Integer transactionId= 1;
		mpt.addToSoldRfids(RFID, transactionId);
		assertTrue(mpt.rfidIsSold(RFID));
		mpt.removeRFIDFromSold(RFID);
		assertFalse(mpt.rfidIsSold(RFID));
		
	}
	@Test
	public void testRemoveAllSoldRfidsOfSale() {
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", 1.00, "");
		String RFID= "123456789000";
		Integer transactionId= 1;
		mpt.addToSoldRfids(RFID, transactionId);
		String RFID1= "123456789010";
		Integer transactionId1= 2;
		String RFID2= "123456789012";
		Integer transactionId2= 3;
		String RFID3= "234567890123";
		Integer transactionId3= 1;
		String RFID4= "345678901234";
		Integer transactionId4= 1;
		mpt.addToSoldRfids(RFID1, transactionId1);
		mpt.addToSoldRfids(RFID2, transactionId2);
		mpt.addToSoldRfids(RFID3, transactionId3);
		mpt.addToSoldRfids(RFID4, transactionId4);
		mpt.removeAllSoldRfidsOfSale(transactionId);
		assertFalse(mpt.rfidIsSold(RFID));
		assertTrue(mpt.rfidIsSold(RFID1));
		assertTrue(mpt.rfidIsSold(RFID2));
		assertFalse(mpt.rfidIsSold(RFID3));
		assertFalse(mpt.rfidIsSold(RFID4));
	}
	@Test
	public void testGetRfidPerSale() {
		MyProductType mpt = new MyProductType(1, "123456789128", "milk", 1.00, "");
		String RFID= "123456789000";
		Integer transactionId= 1;
		mpt.addToSoldRfids(RFID, transactionId);
		String RFID1= "123456789010";
		Integer transactionId1= 2;
		String RFID2= "123456789012";
		Integer transactionId2= 3;
		String RFID3= "234567890123";
		Integer transactionId3= 1;
		String RFID4= "345678901234";
		Integer transactionId4= 1;
		mpt.addToSoldRfids(RFID1, transactionId1);
		mpt.addToSoldRfids(RFID2, transactionId2);
		mpt.addToSoldRfids(RFID3, transactionId3);
		mpt.addToSoldRfids(RFID4, transactionId4); 
		assertEquals(3,mpt.getRfidPerSale(transactionId).size());
		
		
		
		 
	}
}
