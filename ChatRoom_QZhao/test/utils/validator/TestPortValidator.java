package utils.validator;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class TestPortValidator extends TestCase {
	
	@Before
	public void setUp(){
		
	}
	
	@Test
	public void testIsValidPort(){
		
		String port = "6666";
		
		assertTrue(PortValidator.isValidPort(port));
		assertFalse(PortValidator.isValidPort(null));
		assertFalse(PortValidator.isValidPort("NotNumber"));
		assertFalse(PortValidator.isValidPort("1000"));
		assertFalse(PortValidator.isValidPort("70000"));
		
		try {
			ServerSocket s = new ServerSocket(Integer.parseInt(port));
			assertFalse(PortValidator.isValidPort(port));
	    } catch (IOException e) {
	    }
	}
	
}
