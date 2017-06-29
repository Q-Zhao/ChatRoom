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
 * {@link #ClientSide$PrivateChatContentMessageHandler(ClientFrame, ClientChatThread)} is a message handler for private chatting message, which means it is sent by one client privately to another client.
 * It is registered as a message handler node in ClientMessageHandlersChain, which is a linked nodes list in chain of responsibility design model.
 * @author QQZhao
 *
 */
public class ClientSide$PrivateChatContentMessageHandler extends AbstractMessageHandler {

	private ClientFrame clientChatView;
	private ClientChatThread clientChatThread;
	
	public ClientSide$PrivateChatContentMessageHandler(ClientFrame clientChatView, ClientChatThread clientChatThread) {
		this.clientChatView = clientChatView;
		this.clientChatThread = clientChatThread;
	}

	/**{@link #process(String)} method handles a message from server if this message is in type of PRIVATE_CHAT_CONTENT.
	 * If the type of the message is PRIVATE_CHAT_CONTENT, it will update and show this message in client's private chat GUI with the corresponding remote client.
	 * If the type of the message is not PRIVATE_CHAT_CONTENT, the ClientMessageHandlersChain will pass this message to the next registered message handler node.
	 * @param message
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);

		if (!messageType.equals(XmlElementValue.PRIVATE_CHAT_CONTENT)){
			this.nextHandler.process(message);
			return;
		}
		
		String fromUsername = MessageParser.getElementValueByName(message, XmlElementName.FROM_USERNAME);
		String toUsername = MessageParser.getElementValueByName(message, XmlElementName.TO_USERNAME);
		String messageContent = MessageParser.getElementValueByName(message, XmlElementName.MESSAGE);
		
		// update message
		PrivateChatRoom pcr = null;
		if(fromUsername.equals(this.clientChatView.getClient().getUserName())){
			pcr = Chatroom.getChatroomInstance().getThisPrivateChatRoom(fromUsername, toUsername);
		}else{
			pcr = Chatroom.getChatroomInstance().getThisPrivateChatRoom(toUsername, fromUsername);
		}
		
		ClientPrivateFrame targetPrivateFrame  = pcr.getPrivateChatFrame();
		targetPrivateFrame.updatePrivateChatMessages(fromUsername, messageContent);
	}
}
