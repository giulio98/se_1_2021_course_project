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

import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.Catalogue;
import it.polito.ezshop.data.MyProductType;
import it.polito.ezshop.data.MyReturnTransaction;
import it.polito.ezshop.data.MySaleTransaction;
import it.polito.ezshop.data.MyTicketEntry;

public class TestMyReturnTransaction {

    @Test
	public void testReturnProduct() {
		MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 1, 1.00, "milk");
        sale.setStatus("CLOSED");
		
		MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        assertFalse(rt.returnProduct("123456", 5));
        assertFalse(rt.returnProduct("123456789128", 5));
        assertTrue(rt.returnProduct("123456789128", 1));
	}
    
    @Test
    public void testCommit() {
    	Catalogue cat = new Catalogue();
		cat.addProductType("milk", "123456789128", 1.00, "");
		cat.addProductType("coca-cola", "1234567891231", 2.00, "");
		MyProductType pt1 = (MyProductType) cat.getProductTypeByBarCode("123456789128");
		pt1.updatePosition("1-a-1");
		pt1.updateQuantity(20);
		
		MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 1, 1.00, "milk");
        sale.setStatus("CLOSED");
        MyTicketEntry entry = sale.getTicketEntry("123456789128");
        Integer quantity1 = entry.getAmount();
        assertEquals((Integer)1,quantity1);
        
		MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        rt.returnProduct("123456789128", 1);
        rt.commit(cat);
        
        Integer quantity2 = entry.getAmount();
        assertEquals((Integer)0,quantity2);
        
        assertEquals((Integer)19,pt1.getQuantity());
        assertNotEquals((Integer)20,pt1.getQuantity());
	}
    
    @Test
    public void testComputeTotalPrice() {
		MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 3, 1.00, "milk");
        sale.addProductToSale("1234567891231", 5, 2.00, "coca-cola");
        sale.setStatus("CLOSED");
        
		MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        
		rt.returnProduct("123456789128", 2);
        Double price1 = rt.computeTotalPrice();
        assertEquals((Double)2.0,price1);
        
        rt.returnProduct("123456789128", 1);
        Double price2 = rt.computeTotalPrice();
        assertEquals((Double)3.0,price2);
        
        rt.returnProduct("1234567891231", 3);
        Double price3 = rt.computeTotalPrice();
        assertEquals((Double)9.0,price3);
        
	}
    
    @Test
    public void testGetReturnID() {
    	MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 3, 1.00, "milk");
        sale.addProductToSale("1234567891231", 5, 2.00, "coca-cola");
        sale.setStatus("CLOSED");
        
        MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        Integer id = rt.getReturnID();
        assertEquals((Integer)2,id);
    }
    
    @Test
    public void testSetReturnID() {
    	MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 3, 1.00, "milk");
        sale.addProductToSale("1234567891231", 5, 2.00, "coca-cola");
        sale.setStatus("CLOSED");
        
        MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        rt.setReturnID(3);
        Integer id = rt.getReturnID();
        assertEquals((Integer)3,id);
    }
    
    @Test
    public void testGetSaleID() {
    	MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 3, 1.00, "milk");
        sale.addProductToSale("1234567891231", 5, 2.00, "coca-cola");
        sale.setStatus("CLOSED");
        
        MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        Integer id = rt.getSaleID();
        assertEquals((Integer)1,id);
    }
    
    @Test
    public void testSetSaleID() {
    	MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 3, 1.00, "milk");
        sale.addProductToSale("1234567891231", 5, 2.00, "coca-cola");
        sale.setStatus("CLOSED");
        
        MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        rt.setSaleID(5);
        Integer id = rt.getSaleID();
        assertEquals((Integer)5,id);
    }
    
    @Test
    public void testGetSaleTransaction() {
    	MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 3, 1.00, "milk");
        sale.addProductToSale("1234567891231", 5, 2.00, "coca-cola");
        sale.setStatus("CLOSED");
        
        MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        MySaleTransaction same = rt.getSaleTransaction();
        assertEquals(sale,same);
    }
    
    @Test
    public void testGetStatus() {
    	MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 3, 1.00, "milk");
        sale.addProductToSale("1234567891231", 5, 2.00, "coca-cola");
        sale.setStatus("CLOSED");
        
        MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        String same = rt.getStatus();
        assertEquals(null,same);
        
        rt.setStatus("ISSUED");
        String same2 = rt.getStatus();
        assertEquals("ISSUED",same2);
    }
    
    @Test
    public void testSetSaleTransaction() {
    	MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 3, 1.00, "milk");
        sale.addProductToSale("1234567891231", 5, 2.00, "coca-cola");
        sale.setStatus("CLOSED");
        
        MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        rt.setSaleTransaction(null);
        assertEquals(null,rt.getSaleTransaction());
    }
    
    @Test
    public void testSetReturnedPerBarcode() {
    	MySaleTransaction sale = new MySaleTransaction(1);
        sale.addProductToSale("123456789128", 3, 1.00, "milk");
        sale.addProductToSale("1234567891231", 5, 2.00, "coca-cola");
        sale.setStatus("CLOSED");
        
        MyReturnTransaction rt = new MyReturnTransaction(2,sale);
        rt.setReturnedPerBarcode(null);
        assertEquals(null,rt.getReturnedPerBarcode());
    }
    
    
}
