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

import it.polito.ezshop.data.MyProductType;
import it.polito.ezshop.data.ProductCounter;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.Catalogue;

public class TestCatalogue {

	@Test
	public void testAddProductType() {
		Catalogue cat = new Catalogue();
		
		List<ProductType> returnedList = cat.getAllProductTypes();
		assertEquals(Collections.emptyList(), returnedList);
		
		Integer newId = cat.addProductType("milk", "123456789128", 1.00, "");
		Integer id = ProductCounter.getCounter();
		assertEquals(id, newId);
		returnedList = cat.getAllProductTypes();
		assertNotEquals(Collections.emptyList(), returnedList);
		
		ProductType pt = cat.getProductTypeByBarCode("123456789121");
		assertNull(pt);
		
		pt = cat.getProductTypeByBarCode("123456789128");
		assertNotNull(pt);
		
		returnedList = cat.getProductTypesByDescription("milk");
		assertNotEquals(Collections.emptyList(), returnedList);
		
		returnedList = cat.getProductTypesByDescription("lo");
		assertEquals(Collections.emptyList(), returnedList);
		
		pt = cat.getProductTypeById(newId);
		assertNotNull(pt);
	}
	
	@Test
	public void testDeleteProductType() {
		Catalogue cat = new Catalogue();
		Integer newId = cat.addProductType("milk", "123456789128", 1.00, "");
		
		List<ProductType> returnedList = cat.getAllProductTypes();
		assertNotEquals(Collections.emptyList(), returnedList);
		
		cat.deleteProductType(newId);
		returnedList = cat.getAllProductTypes();
		assertEquals(Collections.emptyList(), returnedList);
	}
	
	@Test
	public void testCheckPosition() {
		Catalogue cat = new Catalogue();
		Integer newId = cat.addProductType("milk", "123456789128", 1.00, "");
		Integer newId2 = cat.addProductType("coca-cola", "1234567891231", 2.00, "");
		assertTrue(cat.checkPosition("1-a-1"));
		
		MyProductType mpt = cat.getProductTypeById(newId2);
		mpt.setLocation("1-a-1");
		assertFalse(cat.checkPosition("1-a-1"));
	}
	
	@Test
	public void testCheckBarCode() {
		Catalogue cat = new Catalogue();
		
		assertTrue(cat.checkBarCode("123456789128"));
		assertTrue(cat.checkBarCode("1234567891231"));
		assertTrue(cat.checkBarCode("12345678912343"));
		assertTrue(cat.checkBarCode("1234567891224"));
		assertTrue(cat.checkBarCode("1234567891118"));
		assertTrue(cat.checkBarCode("1234567891200"));
		
		
		assertFalse(cat.checkBarCode("123456789123"));
		assertFalse(cat.checkBarCode("1234567891232"));
		assertFalse(cat.checkBarCode("12345678912345"));
		assertFalse(cat.checkBarCode("1234567891227"));
		assertFalse(cat.checkBarCode("1234567891119"));
	}
	
	 
	@Test
	public void testAddToRfidsProducts() {
		Catalogue cat = new Catalogue();
		Integer newId = cat.addProductType("milk", "123456789128", 1.00, "");
		Integer newId2 = cat.addProductType("coca-cola", "1234567891231", 2.00, "");
		MyProductType mpt1 = cat.getProductTypeById(newId);
		MyProductType mpt2 = cat.getProductTypeById(newId2);
		String RFID= "123456789000";
		String RFID2 = "212345678901";
		cat.addToRfidsProducts(RFID, mpt1);
		cat.addToRfidsProducts(RFID2, mpt2);
		assertEquals(mpt1,cat.getProductTypeByRFID(RFID));
		assertEquals(mpt2,cat.getProductTypeByRFID(RFID2));
	}
}
