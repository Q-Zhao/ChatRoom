package client.controller;


import org.junit.Before;
import org.junit.Test;
import client.view.ClientLogin;
import junit.framework.TestCase;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

public class TestClientChatThread extends TestCase{
	
	ServerSimulator serverSimulator;
	private int testPortNum = 6003;
	private String loginSuccessResponse = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.LOGIN_RESPONSE, XmlElementName.MESSAGE, XmlElementValue.SUCCESS);
	
	@Before
	public void setUp(){
		this.serverSimulator = new ServerSimulator(this.testPortNum);
	}

	@Test
	public void testConnection() {
		
		this.serverSimulator.setResponseMessage(this.loginSuccessResponse);
		serverSimulator.start();
		new ClientChatThread(new ClientLogin(), 
				"TestUserName", "localhost", this.testPortNum);
		
		String clientSentMessage = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.LOGIN_REQUEST, XmlElementName.USERNAME, "TestUserName");
		String serverReceivedMessage = serverSimulator.getClientSentMessage();
		assertEquals(clientSentMessage, serverReceivedMessage);
		
	}

}
