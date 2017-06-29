package server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import server.model.ServerContainer;
import server.model.User;
import server.view.ServerFrame;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

/**
 * The ServerConnectThread handles connection request from new clients.
 * @author QQZhao
 * 
 */
public class ServerConnectThread extends Thread {
	
	private ServerFrame serverFrameView;
	private int port;	
	private ServerSocket serverSocket;
	
	/**
	 * In the {@link #ServerConnectThread(ServerFrame, int)}} constructor, a server socket is allocated.
	 * @param serverFrameView
	 * @param port
	 */	
	public ServerConnectThread(ServerFrame serverFrameView, int port) {		
		this.serverFrameView = serverFrameView;
		this.port = port;		
		try {
			serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			this.serverFrameView.showServerStartFailure();
			System.exit(1);
		}	
	}
	
	/**
	 * The loop in {@link #run()} method keep listening a connection message sent from a potential user.
	 * Once a connection is established, a new socket is formed for this client, and this socket is passed to and managed by a new serverChatThread.
	 */
	@Override
	public void run() {	
		while(true){
			String loginResponse = null;
			try {
				// once accepted, a new socket is formed. The connection is established.
				Socket socket = this.serverSocket.accept();
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				// read connection message from new user.
				byte[] buffer = new byte[4096];
				int length = is.read(buffer);				
				String clientLoginMessage = new String(buffer, 0, length);
				
				//extract information from message.
				String requestType = MessageParser.getElementValueByName(clientLoginMessage, XmlElementName.TYPE);
				String requestUsername = MessageParser.getElementValueByName(clientLoginMessage, XmlElementName.USERNAME);
				
				// validate extracted information.
				if(requestType.equals(XmlElementValue.LOGIN_REQUEST) && ServerContainer.getServerInstance().isValidUserName(requestUsername)){
					
					// login successfully, generate and send response message.
					loginResponse = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.LOGIN_RESPONSE, XmlElementName.MESSAGE, XmlElementValue.SUCCESS);
					os.write(loginResponse.getBytes());
					
					// a new serverChatThread is generated and start managing the socket.
					ServerChatThread serverChatThread = new ServerChatThread(this.serverFrameView, socket);
					
					// update ServerContainer model.
					User user = new User(requestUsername, serverChatThread);															
					ServerContainer.getServerInstance().registerUser(user);					
					String printableUpdatedUsernameList = ServerContainer.getServerInstance().converUserListPrintable();
					this.serverFrameView.updateUserListView(printableUpdatedUsernameList, ServerContainer.getServerInstance().getCurrentUserNameList().size());
					String newServerStatusInfo = String.format("%s connected.", requestUsername);
					this.serverFrameView.updateServerLogTextArea(newServerStatusInfo);
					// Thread set to sleep for a short time to avoid socket sending both loginResponse and usernameList message together.
					Thread.sleep(100);
					
					//send every client the updated username list.
					List<String> updatedUsernameList = ServerContainer.getServerInstance().getCurrentUserNameList();
					String userListMessage = MessageParser.buildUserNameListMessage(XmlElementName.TYPE, XmlElementValue.UPDATED_USERNAME_LIST, 
																					XmlElementName.MESSAGE, updatedUsernameList,
																					XmlElementName.LOG, newServerStatusInfo);
					ServerContainer.getServerInstance().sendEveryUserMessage(userListMessage);					
					serverChatThread.start();
				}else{
					// login failed. Generate and send response message.
					loginResponse = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.LOGIN_RESPONSE, XmlElementName.MESSAGE, XmlElementValue.USER_EXISTS_ERROR);
					os.write(loginResponse.getBytes());
				}
				
			} catch (Exception e) {
			}
		}		
	}
}
