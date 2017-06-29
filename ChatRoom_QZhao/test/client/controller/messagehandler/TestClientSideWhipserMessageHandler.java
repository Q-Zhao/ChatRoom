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

public class TestClientSideWhipserMessageHandler extends TestCase {
	private String testUsername = "TestUsername";
	private String testRemoteUsername = "TestRemoteUsername";
	private final String WISPER_FROM_FORMAT = "[%s whispered to you]: ";
	private Client client;
	ClientSide$WhipserMessageHandler clientSide$WipserMessageHandler;
	ClientFrame clientFrame;
	ClientChatThread clientChatThread;

	@Before
	public void setUp(){
		clientChatThread = new ClientChatThread();
		client = new Client(this.testUsername);
		clientFrame = new ClientFrame(client, clientChatThread, 0, 0);
		clientSide$WipserMessageHandler = 
				new ClientSide$WhipserMessageHandler(clientFrame, clientChatThread);
		Chatroom.getChatroomInstance().setClient(client);
	}
	
	@Test
	public void testProcess(){
		String privateMessageContent = "This is a private message";
		String privateMessage = MessageParser.buildMessage(
				XmlElementName.TYPE, XmlElementValue.WHISPER_MESSAGE, 
				XmlElementName.FROM_USERNAME, this.testRemoteUsername,
				XmlElementName.TO_USERNAME, this.testUsername,
				XmlElementName.MESSAGE, privateMessageContent);
		
		clientSide$WipserMessageHandler.process(privateMessage);
		assertEquals(String.format(WISPER_FROM_FORMAT + privateMessageContent + "\n", this.testRemoteUsername), 
				clientFrame.getShowMsgTextArea().getText());
	}
}
