package server.controller.messagehandler;

import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

import client.view.ClientFrame;
import junit.framework.TestCase;
import server.controller.ServerChatThread;
import server.view.ServerFrame;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

public class TestServerSideClientExitMessageHandler extends TestCase {

	private ServerFrame serverFrame;
	private ServerChatThread serverChatThread;
	private ServerSide$ClientExitMessageHandler serverSide$ClientExitMessageHandler;
	private String fromUsername = "fromUsername";
	
	@Before
	public void setUp(){
		serverFrame = new ServerFrame();
		serverChatThread = new ServerChatThread(serverFrame, new Socket());
		serverSide$ClientExitMessageHandler = new ServerSide$ClientExitMessageHandler(serverFrame, serverChatThread);		
	}
	
	@Test
	public void testProcess(){
		String clientExitMessage = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.CLIENT_EXIT, 
																	XmlElementName.USERNAME, this.fromUsername);
		
		serverSide$ClientExitMessageHandler.process(clientExitMessage);
		String expectedLogInfo = String.format("%s disconnected.", this.fromUsername);
		String actualLogInfo = serverFrame.getServerLogTextArea().getText().trim();
		assertEquals(expectedLogInfo, actualLogInfo);
	}
}
