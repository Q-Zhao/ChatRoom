package client.model;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestChatroom {
	
	Chatroom chatroomSingleton;

	@Before
	public void setUp() {
		chatroomSingleton = Chatroom.getChatroomInstance();		
	}
	
	@Test
	public void testClientInChatroom(){
		chatroomSingleton.setClient(new Client("testClientUsername"));
		assertEquals("testClientUsername", 
					chatroomSingleton.getClient().getUserName());
	}
	
	@Test
	public void testUsernameList(){
		chatroomSingleton.getUsernameList().clear();
		chatroomSingleton.getUsernameList().add("testRemoteUser1");
		chatroomSingleton.getUsernameList().add("testRemoteUser2");
		assertEquals(2, chatroomSingleton.getUsernameList().size());
		Assert.assertArrayEquals(new String[]{"testRemoteUser1", "testRemoteUser2"},
							chatroomSingleton.converUsernameList2Array());
		chatroomSingleton.getUsernameList().add("testClientUsername");
		Assert.assertArrayEquals(new String[]{"testRemoteUser1", "testRemoteUser2", "testClientUsername (self)"},
				chatroomSingleton.converUsernameList2Array());
	}
	
	@Test
	public void testUniqOfPrivateChatRoom(){
		PrivateChatRoom pcr = new PrivateChatRoom("testLocalUsername", "testRemoteUsername");
		chatroomSingleton.addToPrivateChatRoomList(pcr);
		assertTrue(chatroomSingleton.thisPrivateChatRoomExists("testLocalUsername", "testRemoteUsername"));
		assertTrue(pcr.equals(chatroomSingleton.getThisPrivateChatRoom("testLocalUsername", "testRemoteUsername")));
		assertTrue(chatroomSingleton.hasPrivateChat());
		chatroomSingleton.removeFromPrivateChatRoomList(pcr);	
	}
}
