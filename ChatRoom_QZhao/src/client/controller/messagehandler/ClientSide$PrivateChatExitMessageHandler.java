package client.controller.messagehandler;

import client.controller.ClientChatThread;
import client.model.Chatroom;
import client.model.PrivateChatRoom;
import client.view.ClientFrame;
import client.view.ClientPrivateFrame;
import utils.message.AbstractMessageHandler;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;


/**
 * {@link #ClientSide$PrivateChatExitMessageHandler(ClientFrame, ClientChatThread)} is a message handler taking care of private chat terminated by the remote client.
 * It is registered as a message handler node in ClientMessageHandlersChain, which is a linked nodes list in chain of responsibility design model.
 * @author QQZhao
 *
 */
public class ClientSide$PrivateChatExitMessageHandler extends AbstractMessageHandler {

	private ClientFrame clientChatView;
	private ClientChatThread clientChatThread;
	
	public ClientSide$PrivateChatExitMessageHandler(ClientFrame clientChatView, ClientChatThread clientChatThread) {
		this.clientChatView = clientChatView;
		this.clientChatThread = clientChatThread;
	}

	/**{@link #process(String)} method handles a message from server if this message is in type of CLIENT_PRIVATE_EXIT.
	 * If the type of the message is CLIENT_PRIVATE_EXIT, it will remove the corresponding private-chat-room from private-chat-room list as maintained by chatroom. The correpsonding GUI is terminated as well.
	 * If the type of the message is not CLIENT_PRIVATE_EXIT, the ClientMessageHandlersChain will pass this message to the next registered message handler node.
	 * @param message
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);

		if (!messageType.equals(XmlElementValue.CLIENT_PRIVATE_EXIT)){
			this.nextHandler.process(message);
			return;
		}
		
		String remoteUsername = MessageParser.getElementValueByName(message, XmlElementName.FROM_USERNAME);
		String localUsername = MessageParser.getElementValueByName(message, XmlElementName.TO_USERNAME);
		
		PrivateChatRoom pcr = Chatroom.getChatroomInstance().getThisPrivateChatRoom(localUsername, remoteUsername);
		
		//update view
		ClientPrivateFrame targetPrivateFrame  = pcr.getPrivateChatFrame();
		targetPrivateFrame.showRemoteUsernameExit(remoteUsername);
		
		//update model		
		Chatroom.getChatroomInstance().removeFromPrivateChatRoomList(pcr);
	}

}
