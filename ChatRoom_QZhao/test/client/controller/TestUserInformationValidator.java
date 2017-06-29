package client.controller;

import org.junit.Test;

import junit.framework.TestCase;

public class TestUserInformationValidator extends TestCase {
	
	@Test
	public void testIsUserNameInputNotEmpty(){
		UserInformationValidator validator = new UserInformationValidator("", "localhost", "5000");
		assertFalse(validator.isUserNameInputNotEmpty());
	}

	@Test
	public void testIsServerInfoInputNotEmpty(){
		UserInformationValidator validator1 = new UserInformationValidator("username", "", "5000");
		assertFalse(validator1.isServerInfoInputNotEmpty());
		
		UserInformationValidator validator2 = new UserInformationValidator("username", "localhost", "");
		assertFalse(validator2.isServerInfoInputNotEmpty());
		
		UserInformationValidator validator3 = new UserInformationValidator("username", "localhost", "5000");
		assertTrue(validator3.isServerInfoInputNotEmpty());
	}
	
	@Test
	public void testIsServerPortInputValid(){
		UserInformationValidator validator4 = new UserInformationValidator("username", "localhost", "5000");
		assertTrue(validator4.isServerPortInputValid());
		
		UserInformationValidator validator5 = new UserInformationValidator("username", "localhost", "1000");
		assertFalse(validator5.isServerPortInputValid());
		
		UserInformationValidator validator6 = new UserInformationValidator("username", "localhost", "65540");
		assertFalse(validator6.isServerPortInputValid());
		
		UserInformationValidator validator7 = new UserInformationValidator("username", "localhost", "NotNumber");
		assertFalse(validator7.isServerPortInputValid());
	}
}
