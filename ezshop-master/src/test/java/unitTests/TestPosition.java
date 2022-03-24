package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.Position;

public class TestPosition {
	@Test
	public void testSetLocation() {
		Position p = new Position("12-a-1");
		p.setLocation("12-a-1");
		assertEquals("12-a-1",p.getLocation());
		p.setLocation(null);
		assertEquals("",p.getLocation());
		p.setLocation("");
		assertEquals("",p.getLocation());
		
	}

}
