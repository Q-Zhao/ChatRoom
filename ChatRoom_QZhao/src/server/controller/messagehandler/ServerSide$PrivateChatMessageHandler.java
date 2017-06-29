package server.controller.messagehandler;

import server.controller.ServerChatThread;
import server.model.ServerContainer;
import server.view.ServerFrame;
import utils.message.AbstractMessageHandler;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

/**
 * 
 * @author QQZhao
 *
 */
public class ServerSide$PrivateChatMessageHandler extends AbstractMessageHandler {

	private ServerFrame serverFrameView;
	private ServerChatThread serverChatThread;
	
	public ServerSide$PrivateChatMessageHandler(ServerFrame serverFrameView, ServerChatThread serverChatThread) {
		this.serverFrameView = serverFrameView;
		this.serverChatThread = serverChatThread;
	}
	
	/**
	 * {@link #process(String)} handles message with the type of element is either CLIENT_PRIVATE_REQUEST or CLIENT_PRIVATE_RESPONSE or CLIENT_PRIVATE_EXIT or PRIVATE_CHAT_CONTENT.
	 * If the type of the message is not any one of the types mentioned above, the ServerMessageHandlersChain will pass this message to the next registered message handler node. 
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);
		
		if (!messageType.equals(XmlElementValue.CLIENT_PRIVATE_REQUEST) && 
				!messageType.equals(XmlElementValue.CLIENT_PRIVATE_RESPONSE) &&
						!messageType.equals(XmlElementValue.CLIENT_PRIVATE_EXIT) &&
							!messageType.equals(XmlElementValue.PRIVATE_CHAT_CONTENT)){
			this.nextHandler.process(message);
			return;
		}
		
		String fromUsername = MessageParser.getElementValueByName(message, XmlElementName.FROM_USERNAME);
		String toUsername = MessageParser.getElementValueByName(message, XmlElementName.TO_USERNAME);
		
		// forward message to target user.
		ServerContainer.getServerInstance().sendMessageToOneClient(toUsername, message);

		// if it is a chatting content message, need to send to both clients.
		if(messageType.equals(XmlElementValue.PRIVATE_CHAT_CONTENT)){
			ServerContainer.getServerInstance().sendMessageToOneClient(fromUsername, message);
		}
				
		// update server logs		
		String newLog = "";
		if(messageType.equals(XmlElementValue.CLIENT_PRIVATE_REQUEST)){
			newLog = String.format("%s sent a private chat request to %s", fromUsername, toUsername);
		}else if (messageType.equals(XmlElementValue.CLIENT_PRIVATE_RESPONSE)){
			String response = MessageParser.getElementValueByName(message, XmlElementName.MESSAGE);
			if (response.equals(XmlElementValue.ACCEPT)){
				newLog = String.format("%s accepted the private chat with %s", toUsername, fromUsername);
			}else{
				newLog = String.format("%s rejected to chat privately with %s", toUsername, fromUsername);
			}			
		}else if (messageType.equals(XmlElementValue.CLIENT_PRIVATE_EXIT)){
			newLog = String.format("The private chat between %s and %s ended.", fromUsername, toUsername);
		}else{
			newLog = String.format("%s sent a private chat message to %s", fromUsername, toUsername);
		}
		this.serverFrameView.updateServerLogTextArea(newLog);		
	}
}
