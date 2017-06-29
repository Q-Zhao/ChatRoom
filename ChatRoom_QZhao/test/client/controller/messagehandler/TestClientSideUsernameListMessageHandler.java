package client.controller.messagehandler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import client.controller.ClientChatThread;
import client.model.Chatroom;
import client.model.Client;
import client.view.ClientFrame;
import junit.framework.TestCase;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

public class TestClientSideUsernameListMessageHandler extends TestCase {
	private String testUsername = "TestUsername";
	private Client client;
	ClientSide$UsernameListMessageHandler clientSide$UsernameListMessageHandler;
	ClientFrame clientFrame;
	ClientChatThread clientChatThread;

	@Before
	public void setUp(){
		clientChatThread = new ClientChatThread();
		client = new Client(this.testUsername);
		clientFrame = new ClientFrame(client, clientChatThread, 0, 0);
		clientSide$UsernameListMessageHandler = 
				new ClientSide$UsernameListMessageHandler(clientFrame, clientChatThread);
		Chatroom.getChatroomInstance().setClient(client);
	}
	
	@Test
	public void testProcess(){
		List<String> usernameList = new ArrayList<>();
		usernameList.add("TestUser1");
		usernameList.add("TestUser2");
		usernameList.add("TestUser3");
		
		String userListMessage = MessageParser.buildUserNameListMessage(XmlElementName.TYPE, XmlElementValue.UPDATED_USERNAME_LIST, 
				XmlElementName.MESSAGE, usernameList,
				XmlElementName.LOG, "server status info");
		
		clientSide$UsernameListMessageHandler.process(userListMessage);
		assertEquals(Chatroom.getChatroomInstance().getUsernameList(), usernameList);
	}
}
