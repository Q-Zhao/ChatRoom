package client.controller.messagehandler;

import client.controller.ClientChatThread;
import client.view.ClientFrame;
import utils.message.AbstractMessageHandler;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

/**
 * {@link #ClientSide$ChatContentMessageHandler(ClientFrame, ClientChatThread)} is a message handler for regular public chatting message, which means it is sent by one client to public.
 * It is registered as a message handler node in ClientMessageHandlersChain, which is a linked nodes list in chain of responsibility design model.
 * @author QQZhao
 *
 */

public class ClientSide$ChatContentMessageHandler extends AbstractMessageHandler {

	private ClientFrame clientChatView;
	private ClientChatThread clientChatThread;
	
	public ClientSide$ChatContentMessageHandler(ClientFrame clientChatView, ClientChatThread clientChatThread) {
		this.clientChatView = clientChatView;
		this.clientChatThread = clientChatThread;
	}
	

	/**{@link #process(String)} method handles a message from server if this message is in type of CHAT_CONTENT_RESPONSE.
	 * If the type of the message is CHAT_CONTENT_RESPONSE, it will update and show the content of this message in client's chat GUI.
	 * If the type of the message is not CHAT_CONTENT_RESPONSE, the ClientMessageHandlersChain will pass this message to the next registered message handler node.
	 * @param message
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);

		if (!messageType.equals(XmlElementValue.CHAT_CONTENT_RESPONSE)){
			this.nextHandler.process(message);
			return;
		}
		
		String messageFromUsername = MessageParser.getElementValueByName(message, XmlElementName.USERNAME);
		String chatContent = MessageParser.getElementValueByName(message, XmlElementName.MESSAGE);
		
		clientChatView.updateChatMessages(messageFromUsername, chatContent);
	}

}
