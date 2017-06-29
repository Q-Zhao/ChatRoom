package client.controller.messagehandler;

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

public class TestClientSideChatContentMessageHandler extends TestCase {
	
	private String testUsername = "TestUsername";
	private Client client;
	private ClientSide$ChatContentMessageHandler clientSide$ChatContentMessageHandler;
	private ClientFrame clientFrame;
	
	@Before
	public void setUp(){
		
		ClientChatThread clientChatThread = new ClientChatThread();
		client = new Client(this.testUsername);
		clientFrame = new ClientFrame(client, clientChatThread, 0, 0);
		clientSide$ChatContentMessageHandler = 
				new ClientSide$ChatContentMessageHandler(clientFrame, clientChatThread);
		Chatroom.getChatroomInstance().setClient(client);
	}
	
	@Test
	public void testProcess(){
		String chatContent = "This is Chat Content";
		String message = MessageParser.buildMessage(
				XmlElementName.TYPE, XmlElementValue.CHAT_CONTENT_RESPONSE, 
				XmlElementName.USERNAME, this.testUsername,
				XmlElementName.MESSAGE, chatContent);
		clientSide$ChatContentMessageHandler.process(message);
		assertEquals(String.format("%s: %s\n", this.testUsername, chatContent), clientFrame.getShowMsgTextArea().getText());
	}
	
}
