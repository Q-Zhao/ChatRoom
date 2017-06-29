package server.controller.messagehandler;

import server.controller.ServerChatThread;
import server.model.ServerContainer;
import server.view.ServerFrame;
import utils.message.AbstractMessageHandler;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

/**
 * ServerSide$ClientExitMessageHandler handles the last message sent from user before user exit chatting.
 * @author QQZhao
 *
 */
public class ServerSide$ClientExitMessageHandler extends AbstractMessageHandler {

	private ServerFrame serverFrameView;
	private ServerChatThread serverChatThread;
	
	public ServerSide$ClientExitMessageHandler(ServerFrame serverFrameView, ServerChatThread serverChatThread) {
		this.serverFrameView = serverFrameView;
		this.serverChatThread = serverChatThread;
	}
	
	/**
	 * {@link #process(String)} handles message with the type of element is CLIENT_EXIT.
	 * If the type of the message is not CLIENT_EXIT, the ServerMessageHandlersChain will pass this message to the next registered message handler node. 
	 */
	@Override
	public void process(String message) {
		
		String messageType = MessageParser.getElementValueByName(message, XmlElementName.TYPE);		
		if (!messageType.equals(XmlElementValue.CLIENT_EXIT)){
			this.nextHandler.process(message);
			return;
		}
		
		String usernameExit = MessageParser.getElementValueByName(message, XmlElementName.USERNAME);
		
		// update server model data
		ServerContainer.getServerInstance().removeUserByUsername(usernameExit);				
		this.serverChatThread.setServerChatThreadOn(false);
		
		// update server username list
		String printableUpdatedUsernameList = ServerContainer.getServerInstance().converUserListPrintable();
		this.serverFrameView.updateUserListView(printableUpdatedUsernameList, ServerContainer.getServerInstance().getCurrentUserNameList().size());
		
		// update server logs
		String newLog = String.format("%s disconnected.", usernameExit);
		this.serverFrameView.updateServerLogTextArea(newLog);
		
		// send updated username list to client		
		String messageToClient = MessageParser.buildUserNameListMessage(XmlElementName.TYPE, XmlElementValue.UPDATED_USERNAME_LIST, 
																		XmlElementName.MESSAGE, ServerContainer.getServerInstance().getCurrentUserNameList(),
																		XmlElementName.LOG, newLog);
	
		ServerContainer.getServerInstance().sendEveryUserMessage(messageToClient);
	}
}
