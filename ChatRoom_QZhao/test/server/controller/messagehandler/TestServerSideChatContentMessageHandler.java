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

public class TestServerSideChatContentMessageHandler extends TestCase {
	
	private ServerFrame serverFrame;
	private ServerChatThread serverChatThread;
	private ServerSide$ChatContentMessageHandler serverSide$ChatContentMessageHandler;
	private String fromUsername = "fromUsername";
	
	@Before
	public void setUp(){
		serverFrame = new ServerFrame();
		serverChatThread = new ServerChatThread(serverFrame, new Socket());
		serverSide$ChatContentMessageHandler = new ServerSide$ChatContentMessageHandler(serverFrame, serverChatThread);		
	}
	
	@Test
	public void testProcess(){
		String chatContent = "This is the chat content";
		String clientSentMessage = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.CHAT_CONTENT, 
				XmlElementName.USERNAME, this.fromUsername,
				XmlElementName.MESSAGE, chatContent);
		
		serverSide$ChatContentMessageHandler.process(clientSentMessage);
		String expectedLogInfo = String.format("%s sent a public message", this.fromUsername);
		String actualLogInfo = serverFrame.getServerLogTextArea().getText().trim();
		assertEquals(expectedLogInfo, actualLogInfo);
	}
	
}
