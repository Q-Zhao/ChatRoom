package server.controller.messagehandler;

import server.controller.ServerChatThread;
import server.model.ServerContainer;
import server.view.ServerFrame;
import utils.message.AbstractMessageHandler;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

/**
 * ServerSide$ChatContentMessageHandler handles regular chat content message except wisper.
 * @author QQZhao
 *
 */
public class ServerSide$ChatContentMessageHandler extends AbstractMessageHandler {

	private ServerFrame serverFrameView;
	private ServerChatThread serverChatThread;
	
	public ServerSide$ChatContentMessageHandler(ServerFrame serverFrameView, ServerChatThread serverChatThread) {
		this.serverFrameView = serverFrameView;
		this.serverChatThread = serverChatThread;
	}
	
	/**
	 * {@link #process(String)} handles message with the type of element is CHAT_CONTENT.
	 * If the type of the message is not CHAT_CONTENT, the ServerMessageHandlersChain will pass this message to the next registered message handler node.
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);
		
		if (!messageType.equals(XmlElementValue.CHAT_CONTENT)){
			this.nextHandler.process(message);
			return;
		}
		
		// extract the username who send this message, and the message content.
		String messageFromUsername = MessageParser.getElementValueByName(message, XmlElementName.USERNAME);
		String chatContent = MessageParser.getElementValueByName(message, XmlElementName.MESSAGE);
		
		// build response message.
		String messageToClient = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.CHAT_CONTENT_RESPONSE, 
															XmlElementName.USERNAME, messageFromUsername,
															XmlElementName.MESSAGE, chatContent);
		// send response message to all users.
		ServerContainer.getServerInstance().sendEveryUserMessage(messageToClient);
		
		// update server logs
		String newLog = String.format("%s sent a public message", messageFromUsername);
		this.serverFrameView.updateServerLogTextArea(newLog);
	}
}
