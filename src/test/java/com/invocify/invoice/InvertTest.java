package com.invocify.invoice;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InvertTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	 @Test
	    public void testStringInvert_EmptyString(){
	        String input = "";
	        String inverted = Inverter.invert(input);
	        
	        assertEquals("", inverted);
	        
	        
	    }

}
