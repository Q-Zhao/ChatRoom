package server.model;


import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

public class TestServerContainer extends TestCase{
	
	ServerContainer serverContainer;
	
	@Before
	public void setUp(){
		serverContainer = ServerContainer.getServerInstance();
		serverContainer.clearAllUsers();
	}
	
	@Test
	public void testIsValidUsername(){
		User user1 = new User("user1");
		serverContainer.registerUser(user1);
		assertFalse(serverContainer.isValidUserName(""));
		assertFalse(serverContainer.isValidUserName(null));
		assertFalse(serverContainer.isValidUserName("user1"));
	}
	
	@Test
	public void testGetCurrentUserNameList(){
		User user1 = new User("user1");
		serverContainer.registerUser(user1);
		assertEquals(1, serverContainer.getCurrentUserNameList().size());
		assertTrue(serverContainer.getCurrentUserNameList() instanceof ArrayList);
	}
	
	@Test
	public void testRemoveUserByUsername(){
		User user1 = new User("user1");
		serverContainer.registerUser(user1);
		serverContainer.removeUserByUsername("user1");
		assertEquals(0, serverContainer.getCurrentUserNameList().size());
	}
	
	@Test
	public void testConvertUserListPrintable(){
		serverContainer.registerUser(new User("user1"));
		serverContainer.registerUser(new User("user2"));
		serverContainer.registerUser(new User("user3"));
		assertEquals("user1\nuser2\nuser3", serverContainer.converUserListPrintable());
	}
}
