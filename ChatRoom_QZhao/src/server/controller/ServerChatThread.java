package server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import server.view.ServerFrame;
import server.controller.messagehandler.ServerSide$ClientExitMessageHandler;
import server.controller.messagehandler.ServerSide$ChatContentMessageHandler;
import server.controller.messagehandler.ServerSide$WhisperMessageHandler;
import server.controller.messagehandler.ServerSide$PrivateChatMessageHandler;
import server.model.User;

/**
 * A instance of ServerChatThread class, along with the corresponding socket, is assigned to a specific user.
 * This instance manages, analyzes and handles all communication messages between this user and server.
 * @author QQZhao
 *
 */
public class ServerChatThread extends Thread {
	
	private ServerFrame serverFrameView;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private ServerMessageHandlersChain messageHandlerChain;
	private User belongsToUser;
	public boolean serverChatThreadOn;

	/**
	 * The thread keeps listening message sent from client if serverChatThreadOn flag is true;
	 * @param serverChatThreadOn
	 */
	public void setServerChatThreadOn(boolean serverChatThreadOn) {
		this.serverChatThreadOn = serverChatThreadOn;
	}
	
	public User getBelongsToUser() {
		return belongsToUser;
	}

	public void setBelongsToUser(User belongsToUser) {
		this.belongsToUser = belongsToUser;
	}

	public ServerChatThread(ServerFrame serverFrameView, Socket socket){
		this.serverChatThreadOn = true;
		this.serverFrameView = serverFrameView;
		this.socket = socket;
		try {
			is = this.socket.getInputStream();
			os = this.socket.getOutputStream();
		} catch (IOException e) {
		}		
	}
	
	/**
	 * {@link #sendMessage(String)} method send message to the corresponding user this ServerChatThread belongs to (or manages).
	 * @param message can be any type of messages.
	 */
	public void sendMessage(String message){
		
		try {
			this.os.write(message.getBytes());
		} catch (IOException e) {
		}
	}
	
	/**
	 * The {@link #run()} method contains a while loop, which keep listens the message sent from the specific client it communicates with.
	 * Once a new message is received, it will pass this message to the MessageHandlerChains, which take care of all the message handling. 
	 * After that, it will keep listening the new message over and over again until a flag named clientChatThreadOn is turned off as false.
	 */	
	@Override
	public void run() {
		
		// message handlers chains are initiated.
		startMessageHandlerChains();
		
		while (this.serverChatThreadOn){
			
			try {								
				byte[] buffer = new byte[4096];
				int length = is.read(buffer);
				String messageFromClient = new String(buffer, 0, length);
				
				// once message received, the message handlers chain will process it.
				messageHandlerChain.process(messageFromClient);
				
				// serverChatThreadOn will be set false during message processing if this user exit from chat room.
				// Then, the loop will break to avoid socket close exception.
				if(!this.serverChatThreadOn){
					is.close();
					os.close();
					socket.close();
					break;
				}
								
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}		
	}
	
	/**
	 * {@link #startMessageHandlerChains()} method initiates a ServerMessageHandlersChain, which has a linked list structure.
	 * Different MessageHandlers are registered on this chain as linked nodes.
	 * Different type of messages are handled by different nodes on the chain.
	 * It follows a last-register-first-node model.
	 */
	private void startMessageHandlerChains(){
		this.messageHandlerChain = new ServerMessageHandlersChain(this.serverFrameView);
		this.messageHandlerChain.register(new ServerSide$ClientExitMessageHandler(this.serverFrameView, this));
		this.messageHandlerChain.register(new ServerSide$ChatContentMessageHandler(this.serverFrameView, this));
		this.messageHandlerChain.register(new ServerSide$WhisperMessageHandler(this.serverFrameView, this));
		this.messageHandlerChain.register(new ServerSide$PrivateChatMessageHandler(this.serverFrameView, this));					
	}	
}
