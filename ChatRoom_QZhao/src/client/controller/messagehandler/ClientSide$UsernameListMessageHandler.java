package client.controller.messagehandler;

import java.util.List;

import client.controller.ClientChatThread;
import client.model.Chatroom;
import client.view.ClientFrame;
import utils.message.AbstractMessageHandler;
import utils.message.XmlElementValue;
import utils.message.MessageParser;
import utils.message.XmlElementName;


/**
 * {@link #ClientSide$UsernameListMessageHandler(ClientFrame, ClientChatThread)} is a message handler taking care of updating the list of username currently in the chatting room. 
 * This message will only be received once some new client joining in or an existing client exit the chat room.
 * It is registered as a message handler node in ClientMessageHandlersChain, which is a linked nodes list in chain of responsibility design model.
 * @author QQZhao
 *
 */
public class ClientSide$UsernameListMessageHandler extends AbstractMessageHandler {
	
	private ClientFrame clientChatView;
	private ClientChatThread clientChatThread;
	
	public ClientSide$UsernameListMessageHandler(ClientFrame clientChatView, ClientChatThread clientChatThread) {
		this.clientChatView = clientChatView;
		this.clientChatThread = clientChatThread;
	}

	/**{@link #process(String)} method handles a message from server if this message is in type of UPDATED_USERNAME_LIST.
	 * If the type of the message is UPDATED_USERNAME_LIST, it will update the view of username list in chatroom GUI.
	 * If the type of the message is not any of above types, the ClientMessageHandlersChain will pass this message to the next registered message handler node.
	 * @param message
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);
		
		if (!messageType.equals(XmlElementValue.UPDATED_USERNAME_LIST)){
			this.nextHandler.process(message);
			return;
		}
		
		List<String> updatedUsernameList = MessageParser.getElementsListValueByName(message, XmlElementName.MESSAGE);
		String newServerStatusInfo = MessageParser.getElementValueByName(message, XmlElementName.LOG);
		Chatroom.getChatroomInstance().setUsernameList(updatedUsernameList);
		String[] usernameArray = Chatroom.getChatroomInstance().converUsernameList2Array();
		this.clientChatView.refreshUsernameListView(usernameArray);		
		this.clientChatView.AddToChatRoomStatusTextArea(newServerStatusInfo);
	}
}
