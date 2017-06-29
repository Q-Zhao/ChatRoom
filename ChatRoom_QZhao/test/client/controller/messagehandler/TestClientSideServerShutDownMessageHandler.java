package client.controller.messagehandler;

import java.util.HashMap;

import javax.swing.JOptionPane;

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

public class TestClientSideServerShutDownMessageHandler extends TestCase {
	
	private String testUsername = "TestUsername";
	private Client client;
	private ClientSide$ServerShutDownMessageHandler clientSide$ServerShutDownMessageHandler;
	private ClientFrame clientFrame;
	static int systemExitCode = 999;
	
	@Before
	public void setUp(){
		
		ClientChatThread clientChatThread = new ClientChatThread();
		client = new Client(this.testUsername);
		clientFrame = new ClientFrameWithNoSystemExit(client, clientChatThread, 0, 0);
		clientSide$ServerShutDownMessageHandler = 
				new ClientSide$ServerShutDownMessageHandler(clientFrame, clientChatThread);
		Chatroom.getChatroomInstance().setClient(client);
	}
	
	@Test
	public void testProcess(){
		String message = "<root><type>server_shut_down</type></root>";
		clientSide$ServerShutDownMessageHandler.process(message);
		assertEquals(0, TestClientSideServerShutDownMessageHandler.systemExitCode);
	}
}

class ClientFrameWithNoSystemExit extends ClientFrame{

	public ClientFrameWithNoSystemExit(Client client, ClientChatThread clientChatThread, int x, int y) {
		super(client, clientChatThread, x, y);
	}

	@Override
	public void showShutDown() {
		JOptionPane.showMessageDialog(this.getFrame(), "Server is shutdown.\nPress OK to exit chatting", "Server Shut Down", JOptionPane.OK_OPTION);
		TestClientSideServerShutDownMessageHandler.systemExitCode = 0;
	}
}