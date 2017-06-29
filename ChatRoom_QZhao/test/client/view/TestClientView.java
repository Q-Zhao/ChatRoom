package client.view;

import static org.junit.Assert.*;

import org.junit.Test;

import client.ClientLauncher;
import client.controller.ClientChatThread;
import client.model.Client;
import client.model.PrivateChatRoom;

public class TestClientView {

	@Test
	public void testClientLoginView() {
		ClientLogin clientLogin = new ClientLogin();
		clientLogin.textField.setText("");
		clientLogin.textField_1.setText("");
		clientLogin.textField_1.setText("");
		clientLogin.validateUserInput();
		clientLogin.textField.setText("TestUsername");
		clientLogin.validateUserInput();
		clientLogin.textField_1.setText("localhost");
		clientLogin.validateUserInput();
		clientLogin.textField_2.setText("500000");
		clientLogin.validateUserInput();
		clientLogin.showConnectionServerAddressFailed();
		clientLogin.showUserNameAlreadyExists();
		clientLogin.showUnexpectedFaiture();
	}
	
	@Test
	public void testSendingRequestNoticeFrame(){
		new SendingRequestNoticeFrame("TestUsername", 10, 10);
	}
	
	@Test
	public void testClientChatFrame(){
		Client client = new Client("testUsername");
		ClientChatThread clientChatThread = new ClientChatThread(new ClientLogin(),
											"testUsername", "localhost", 5000);
		ClientFrame clientFrame = new ClientFrame(client, clientChatThread, 10, 10);
		
		clientFrame.showReceivingRejectiononPrivateChatRequest("TestRemoteUserName");
		clientFrame.showCanNotHandleMessageError();
		
		clientFrame.getShowMsgTextArea().setText("TestUser1: oldMessage.\n");
		clientFrame.updateChatMessages("TestUser2", "newMessage.");
		assertEquals("TestUser1: oldMessage.\nTestUser2: newMessage.\n", clientFrame.getShowMsgTextArea().getText());
		
		String[] expectedUsernameArray = new String[]{"TestUser1", "TestUser2"};
		clientFrame.refreshUsernameListView(expectedUsernameArray);
		assertEquals(expectedUsernameArray.length, clientFrame.getShowUserNameTextList().getModel().getSize());
		for(int i = 0; i < expectedUsernameArray.length; i++){
			String actualUsername = clientFrame.getShowUserNameTextList().getModel().getElementAt(i);
			String expectedUsername = expectedUsernameArray[i];
			assertEquals(expectedUsername, actualUsername);
		}
		
		clientFrame.getClearHistoryBtn().doClick();
		assertEquals("", clientFrame.getShowMsgTextArea().getText());
		
		clientFrame.getClearChatRoomStatusBtn().doClick();
		assertEquals("", clientFrame.getShowChatRoomStatusTextArea().getText());
	}
	
	@Test
	public void testClientPrivateChatFrame(){
		PrivateChatRoom privateChatRoom = new PrivateChatRoom("testLocalUser", "testRemoteUser");
		ClientChatThread clientChatThread = new ClientChatThread(new ClientLogin(),
				"testUsername", "localhost", 5000);
		new ClientPrivateFrame(privateChatRoom, clientChatThread, 10, 10);	
	}

}
