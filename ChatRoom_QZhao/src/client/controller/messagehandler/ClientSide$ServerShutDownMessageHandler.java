package client.controller.messagehandler;

import client.controller.ClientChatThread;
import client.view.ClientFrame;
import utils.message.AbstractMessageHandler;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;


/**
 * {@link #ClientSide$ServerShutDownMessageHandler(ClientFrame, ClientChatThread)} is a message handler taking care of the message sent by server indicating the server has beens shut down. 
 * It is registered as a message handler node in ClientMessageHandlersChain, which is a linked nodes list in chain of responsibility design model.
 * @author QQZhao
 *
 */
public class ClientSide$ServerShutDownMessageHandler extends AbstractMessageHandler {

	private ClientFrame clientChatView;
	private ClientChatThread clientChatThread;
	
	public ClientSide$ServerShutDownMessageHandler(ClientFrame clientChatView, ClientChatThread clientChatThread) {
		this.clientChatView = clientChatView;
		this.clientChatThread = clientChatThread;
	}

	/**{@link #process(String)} method handles a message from server if this message is in type of SERVER_SHUTDOWN.
	 * If the type of the message is SERVER_SHUTDOWN, it will close chatting room GUI.
	 * If the type of the message is not any of above types, the ClientMessageHandlersChain will pass this message to the next registered message handler node.
	 * @param message
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);

		if (!messageType.equals(XmlElementValue.SERVER_SHUTDOWN)){
			this.nextHandler.process(message);
			return;
		}
		this.clientChatThread.setClientChatThreadOn(false);
		this.clientChatView.showShutDown();
	}

}
