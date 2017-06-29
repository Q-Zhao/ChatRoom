package server.controller.messagehandler;

import server.controller.ServerChatThread;
import server.model.ServerContainer;
import server.view.ServerFrame;
import utils.message.AbstractMessageHandler;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

/**
 * ServerSide$WisperMessageHandler handles the wisper message sent from user.
 * @author QQZhao
 *
 */
public class ServerSide$WhisperMessageHandler extends AbstractMessageHandler {

	private ServerFrame serverFrameView;
	private ServerChatThread serverChatThread;
	
	public ServerSide$WhisperMessageHandler(ServerFrame serverFrameView, ServerChatThread serverChatThread) {
		this.serverFrameView = serverFrameView;
		this.serverChatThread = serverChatThread;
	}
	
	/**
	 * {@link #process(String)} handles message with the type of element is WISPER_MESSAGE.
	 * If the type of the message is not WISPER_MESSAGE, the ServerMessageHandlersChain will pass this message to the next registered message handler node. 
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);
		
		if (!messageType.equals(XmlElementValue.WHISPER_MESSAGE)){
			this.nextHandler.process(message);
			return;
		}
		
		// extract the usernames this wisper mesage was sent from and sent to.
		String wisperFromUsername = MessageParser.getElementValueByName(message, XmlElementName.FROM_USERNAME);
		String wisperToUsername = MessageParser.getElementValueByName(message, XmlElementName.TO_USERNAME);
		
		// Forward the whole message to both usernames involved.
		ServerContainer.getServerInstance().sendMessageToOneClient(wisperFromUsername, message);
		ServerContainer.getServerInstance().sendMessageToOneClient(wisperToUsername, message);
		
		// update server logs
		String newLog = String.format("%s whispered to %s", wisperFromUsername, wisperToUsername);
		this.serverFrameView.updateServerLogTextArea(newLog);
	}

}
