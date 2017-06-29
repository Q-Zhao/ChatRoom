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

public class TestServerSideWhisperMessageHandler extends TestCase {
	private ServerFrame serverFrame;
	private ServerChatThread serverChatThread;
	private ServerSide$WhisperMessageHandler serverSide$WisperMessageHandler;
	private String fromUsername = "fromUsername";
	private String toUsername = "toUsername";
	
	@Before
	public void setUp(){
		serverFrame = new ServerFrame();
		serverChatThread = new ServerChatThread(serverFrame, new Socket());
		serverSide$WisperMessageHandler = new ServerSide$WhisperMessageHandler(serverFrame, serverChatThread);		
	}
	
	@Test
	public void testProcess(){
		String chatContent = "This is a whisper message";
		String wisperMessage = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.WHISPER_MESSAGE, 
																	XmlElementName.FROM_USERNAME, this.fromUsername,
																	XmlElementName.TO_USERNAME, this.toUsername,
																	XmlElementName.MESSAGE, chatContent);
		
		serverSide$WisperMessageHandler.process(wisperMessage);
		String expectedLogInfo = String.format("%s whispered to %s", fromUsername, toUsername);
		String actualLogInfo = serverFrame.getServerLogTextArea().getText().trim();
		assertEquals(expectedLogInfo, actualLogInfo);
	}
}
