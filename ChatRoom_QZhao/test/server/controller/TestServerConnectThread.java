package server.controller;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import server.view.ServerFrame;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

public class TestServerConnectThread extends TestCase {
	
	private ServerConnectThread serverConnectThread;
	private ServerFrame serverFrame;
	private String serverAddress = "localhost";
	private int serverPort = 5000;
	private String testUsername = "TestUsername";
	
	@Before
	public void setUp(){
		serverFrame = new ServerFrame();
		serverConnectThread = new ServerConnectThread(serverFrame, serverPort);
		serverConnectThread.start();
	}
	
	@Test
	public void testClientConnection(){
		
		String clientSentMessage = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.LOGIN_REQUEST, 
																XmlElementName.USERNAME, this.testUsername);
		
		ClientSimulator clientSimulator = new ClientSimulator(this.serverAddress, this.serverPort);		
		clientSimulator.sendMessage(clientSentMessage);
		
		String actualResponseMessage = clientSimulator.getResponseMessage();
		String expectedResponseMessage = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.LOGIN_RESPONSE, XmlElementName.MESSAGE, XmlElementValue.SUCCESS);

		assertEquals(expectedResponseMessage, actualResponseMessage);
	}
	
}
