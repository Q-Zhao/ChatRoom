package client.controller.messagehandler;

import org.junit.Before;
import org.junit.Test;

import client.controller.ClientChatThread;
import client.model.Chatroom;
import client.model.Client;
import client.model.PrivateChatRoom;
import client.view.ClientFrame;
import client.view.ClientPrivateFrame;
import junit.framework.TestCase;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

public class TestClientSidePrivateChatExitMessageHandler extends TestCase {
	
	private String testUsername = "TestUsername";
	private ClientSide$PrivateChatExitMessageHandler clientSide$PrivateChatExitMessageHandler;
	private ClientFrame clientFrame;
	private ClientChatThread clientChatThread;
	private ClientPrivateFrame clientPrivateFrame;
	private Client client;
	
	@Before
	public void setUp(){

		clientChatThread = new ClientChatThread();
		client = new Client(this.testUsername);
		clientFrame = new ClientFrame(client, clientChatThread, 0, 0);
		clientSide$PrivateChatExitMessageHandler = 
				new ClientSide$PrivateChatExitMessageHandler(clientFrame, clientChatThread);
		Chatroom.getChatroomInstance().setClient(client);
	}
	
	
	@Test
	public void testProcess(){
		String message = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.CLIENT_PRIVATE_EXIT, 
				XmlElementName.FROM_USERNAME, "TestRemoteUsername",
				XmlElementName.TO_USERNAME, this.testUsername);
		
		PrivateChatRoom privateChatRoom = new PrivateChatRoom(this.testUsername, "TestRemoteUsername");

		clientPrivateFrame = new ClientPrivateFrame(privateChatRoom, 
												clientChatThread, 10, 10);
		
		privateChatRoom.setPrivateChatFrame(clientPrivateFrame);
		
		clientSide$PrivateChatExitMessageHandler.process(message);

	}
	
}
