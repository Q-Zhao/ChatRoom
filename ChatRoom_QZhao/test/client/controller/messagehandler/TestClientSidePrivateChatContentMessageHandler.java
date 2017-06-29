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

public class TestClientSidePrivateChatContentMessageHandler extends TestCase {
	private String testUsername = "TestUsername";
	private ClientSide$PrivateChatContentMessageHandler clientSide$PrivateChatContentMessageHandler;
	private ClientFrame clientFrame;
	private ClientChatThread clientChatThread;
	private ClientPrivateFrame clientPrivateFrame;
	private Client client;

	@Before
	public void setUp(){
		clientChatThread = new ClientChatThread();
		client = new Client(this.testUsername);
		clientFrame = new ClientFrame(client, clientChatThread, 0, 0);
		clientSide$PrivateChatContentMessageHandler = 
				new ClientSide$PrivateChatContentMessageHandler(clientFrame, clientChatThread);
		Chatroom.getChatroomInstance().setClient(client);
	}

	@Test
	public void testProcess(){
		String chatContent = "This is Private Chat Content";
		String message = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.PRIVATE_CHAT_CONTENT, 
				XmlElementName.FROM_USERNAME, this.testUsername,
				XmlElementName.TO_USERNAME, "TestRemoteUsername",
				XmlElementName.MESSAGE, chatContent);
		PrivateChatRoom privateChatRoom = new PrivateChatRoom(this.testUsername, "TestRemoteUsername");
		clientPrivateFrame = new ClientPrivateFrame(privateChatRoom, 
												clientChatThread, 10, 10);
		privateChatRoom.setPrivateChatFrame(clientPrivateFrame);
		
		clientSide$PrivateChatContentMessageHandler.process(message);
		assertEquals(String.format("%s: %s\n", this.testUsername, chatContent), clientPrivateFrame.getPrivateChatShowMessageArea().getText());
	}
}
