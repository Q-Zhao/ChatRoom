package client.controller.messagehandler;

import client.controller.ClientChatThread;
import client.view.ClientFrame;
import utils.message.AbstractMessageHandler;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;


/**
 * {@link #ClientSide$WipserMessageHandler(ClientFrame, ClientChatThread)} is a message handler taking care of a wisper message sent from on one client to another.
 * The difference between private chat message and wisper message is that wisper message will still be shown and only be shown in the corresponding two clients' public message text area. 
 * It is registered as a message handler node in ClientMessageHandlersChain, which is a linked nodes list in chain of responsibility design model.
 * @author QQZhao
 *
 */
public class ClientSide$WhipserMessageHandler extends AbstractMessageHandler {

	private ClientFrame clientChatView;
	private ClientChatThread clientChatThread;
	
	public ClientSide$WhipserMessageHandler(ClientFrame clientChatView, ClientChatThread clientChatThread) {
		this.clientChatView = clientChatView;
		this.clientChatThread = clientChatThread;
	}

	/**{@link #process(String)} method handles a message from server if this message is in type of WISPER_MESSAGE.
	 * If the type of the message is WISPER_MESSAGE, it will update the GUI of chat text area of the corresponding clients.
	 * If the type of the message is not any of above types, the ClientMessageHandlersChain will pass this message to the next registered message handler node.
	 * @param message
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);

		if (!messageType.equals(XmlElementValue.WHISPER_MESSAGE)){
			this.nextHandler.process(message);
			return;
		}
		
		String messageFromUsername = MessageParser.getElementValueByName(message, XmlElementName.FROM_USERNAME);
		String messageToUsername = MessageParser.getElementValueByName(message, XmlElementName.TO_USERNAME);		
		String chatContent = MessageParser.getElementValueByName(message, XmlElementName.MESSAGE);
		
		clientChatView.showWisperMessages(messageFromUsername, messageToUsername, chatContent);
	}

}
