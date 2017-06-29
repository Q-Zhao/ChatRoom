package client.controller.messagehandler;

import client.controller.ClientChatThread;
import client.model.PrivateChatRoom;
import client.view.ClientFrame;
import client.view.ClientPrivateFrame;
import utils.message.AbstractMessageHandler;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;


/**
 * {@link #ClientSide$PrivateChatRequestMessageHandler(ClientFrame, ClientChatThread)} is a message handler taking care of one client's request or respond to start a private chat with another client. 
 * It is registered as a message handler node in ClientMessageHandlersChain, which is a linked nodes list in chain of responsibility design model.
 * @author QQZhao
 *
 */
public class ClientSide$PrivateChatRequestMessageHandler extends AbstractMessageHandler {

	private ClientFrame clientChatView;
	private ClientChatThread clientChatThread;
	
	public ClientSide$PrivateChatRequestMessageHandler(ClientFrame clientChatView, ClientChatThread clientChatThread) {
		this.clientChatView = clientChatView;
		this.clientChatThread = clientChatThread;
	}

	/**{@link #process(String)} method handles a message from server if this message is in type of CLIENT_PRIVATE_REQUEST or CLIENT_PRIVATE_RESPONSE.
	 * If the type of the message is CLIENT_PRIVATE_REQUEST, it will ask the client accept or reject a private chat request from another remote client.
	 * If the type of the message is CLIENT_PRIVATE_RESPONSE, which means this client has sent a private chat request earlier, the handler will either start a private chat or terminate the request based on remote client's response.
	 * If the type of the message is not any of above types, the ClientMessageHandlersChain will pass this message to the next registered message handler node.
	 * @param message
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);

		if (!messageType.equals(XmlElementValue.CLIENT_PRIVATE_REQUEST) &&
				!messageType.equals(XmlElementValue.CLIENT_PRIVATE_RESPONSE)){
			this.nextHandler.process(message);
			
			return;
		}
		String remoteUsername = MessageParser.getElementValueByName(message, XmlElementName.FROM_USERNAME);
		String localUsername = MessageParser.getElementValueByName(message, XmlElementName.TO_USERNAME);
		
		// it is a request message sent from others
		if(messageType.equals(XmlElementValue.CLIENT_PRIVATE_REQUEST)){
			clientChatView.showReceivingPrivateChatRequest(remoteUsername);
		}
		
		// it is a response message to what this user sent earlier
		else{
			if(MessageParser.getElementValueByName(message, XmlElementName.MESSAGE).equals(XmlElementValue.ACCEPT)){
				PrivateChatRoom privateChatRoom = new PrivateChatRoom(localUsername, remoteUsername);
				ClientPrivateFrame privateChatFrame = new ClientPrivateFrame(privateChatRoom, 
															clientChatThread,
															this.clientChatView.getFrame().getX() + this.clientChatView.getFrame().getWidth(), 
															this.clientChatView.getFrame().getY());
				privateChatFrame.setChatWithNameLabelText(remoteUsername);
				privateChatFrame.setMyNameLabelText(localUsername);
				privateChatRoom.setPrivateChatFrame(privateChatFrame);
				privateChatFrame.setVisible(true);
			}else{
				clientChatView.showReceivingRejectiononPrivateChatRequest(remoteUsername);
			}
			
			clientChatView.getSendingPrivateRequestFramesMap().get(remoteUsername).setVisible(false);
			clientChatView.getSendingPrivateRequestFramesMap().remove(remoteUsername);			
		}
	}

}
