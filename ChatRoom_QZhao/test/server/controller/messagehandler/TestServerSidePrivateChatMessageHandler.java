package server.controller.messagehandler;

import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import server.controller.ServerChatThread;
import server.view.ServerFrame;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

public class TestServerSidePrivateChatMessageHandler extends TestCase {
	private ServerFrame serverFrame;
	private ServerChatThread serverChatThread;
	private ServerSide$PrivateChatMessageHandler serverSide$PrivateChatMessageHandler;
	private String fromUsername = "fromUsername";
	private String toUsername = "toUsername";
	
	@Before
	public void setUp(){
		serverFrame = new ServerFrame();
		serverChatThread = new ServerChatThread(serverFrame, new Socket());
		serverSide$PrivateChatMessageHandler = new ServerSide$PrivateChatMessageHandler(serverFrame, serverChatThread);		
	}
	
	@Test
	public void testProcess(){
		String chatContent = "This is a private message";
		String privateMessage = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.PRIVATE_CHAT_CONTENT, 
																	XmlElementName.FROM_USERNAME, this.fromUsername,
																	XmlElementName.TO_USERNAME, this.toUsername,
																	XmlElementName.MESSAGE, chatContent);
		
		serverSide$PrivateChatMessageHandler.process(privateMessage);
		String expectedLogInfo = String.format("%s sent a private chat message to %s", fromUsername, toUsername);
		String actualLogInfo = serverFrame.getServerLogTextArea().getText().trim();
		assertEquals(expectedLogInfo, actualLogInfo);
	}
}
