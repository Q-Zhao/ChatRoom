package client.controller;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import client.controller.messagehandler.ClientSide$ServerShutDownMessageHandler;
import client.controller.messagehandler.ClientSide$UsernameListMessageHandler;
import client.controller.messagehandler.ClientSide$ChatContentMessageHandler;
import client.controller.messagehandler.ClientSide$WhipserMessageHandler;
import client.controller.messagehandler.ClientSide$PrivateChatExitMessageHandler;
import client.controller.messagehandler.ClientSide$PrivateChatRequestMessageHandler;
import client.controller.messagehandler.ClientSide$PrivateChatContentMessageHandler;
import client.model.Chatroom;
import client.model.Client;
import client.view.ClientFrame;
import client.view.ClientLogin;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;


/**
 * ClientChatThread is a subclass of Thread. It takes care of all the communication between client and server.
 * @author QQZhao
 *
 */
public class ClientChatThread extends Thread {
	
	private ClientLogin clientLoginView;
	private ClientFrame clientChatView;
	private ClientMessageHandlersChain messageHandlersChain;
	private String userName;
	private String serverAddress;
	private int serverPort;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private boolean clientChatThreadOn;

	public void setClientChatThreadOn(boolean clientChatThreadOn) {
		this.clientChatThreadOn = clientChatThreadOn;
	}
	
	public ClientChatThread(){}
	
	/**
	 * The constructor of ClientChatThread will try to connect to the specific server and sending the login information to server.
	 * Once obtained the response from server, it will either authorize the client to login to the chat room or rejected by giving the corresponding reason.
	 * @param clientLoginView
	 * @param userName
	 * @param serverAddress
	 * @param port
	 */
	public ClientChatThread(ClientLogin clientLoginView, String userName, String serverAddress, int port) {
		this.clientLoginView = clientLoginView;
		this.userName = userName;
		this.serverAddress = serverAddress;
		this.serverPort = port;		
		try {
			// try to connect to server
			InetAddress serverHost = InetAddress.getByName(this.serverAddress);
			socket = new Socket(serverHost, this.serverPort);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			
			// The first thing after connect successfully is sending the login message to server.
			String loginRequestMessage = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.LOGIN_REQUEST, XmlElementName.USERNAME, this.userName);		
			os.write(loginRequestMessage.getBytes());
			
			// Obtaining the login response message from server.
			byte[] buffer = new byte[4096];
			int length = is.read(buffer);
			String loginResponseMessage = new String(buffer, 0, length);
			
			// Handling the login response message.
			String responseType = MessageParser.getElementValueByName(loginResponseMessage, XmlElementName.TYPE);
			String responseMessage = MessageParser.getElementValueByName(loginResponseMessage, XmlElementName.MESSAGE);
			
			// login successfully
			if (responseType.equals(XmlElementValue.LOGIN_RESPONSE) && responseMessage.equals(XmlElementValue.SUCCESS)){				
				this.clientChatThreadOn = true;
				// a formal client is formed and bind to chatroom as one-to-one.
				Client client = new Client(this.userName);
				Chatroom.getChatroomInstance().setClient(client);
				startChatRoomView(client, socket);					
			}
			// login failed because of duplicated username
			else if (responseType.equals(XmlElementValue.LOGIN_RESPONSE) && responseMessage.equals(XmlElementValue.USER_EXISTS_ERROR)){
				this.clientLoginView.showUserNameAlreadyExists();
				is.close();
				os.close();
				socket.close();
				return;
			}
			// login failed because of all the other situations.
			else{
				this.clientLoginView.showUnexpectedFaiture();
				is.close();
				os.close();
				socket.close();
				return;
			}								
		} catch (UnknownHostException e) {
			this.clientLoginView.showConnectionServerAddressFailed();
		} catch (IOException e) {
			this.clientLoginView.showConnectionPortFailed();
			}				
	}
	
	/**
	 * The {@link #sendMessage(String)} method sends a message to the server currently connected via the active socket. 
	 * @param message
	 */
	public void sendMessage(String message){
		try {
			this.os.write(message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The {@link #run()} method contains a while loop, which keep listens the message sent from server.
	 * Once a new message is received, it will pass this message to the MessageHandlerChains, which take care of all the message handling. 
	 * After that, it will keep listening the new message over and over again until a flag named clientChatThreadOn is turned off as false.
	 */	
	@Override
	public void run() {
		
		// initiate the MessageHandlersChain registration.
		startMessageHandlerChains();
		
		while (this.clientChatThreadOn){
			try {
				// listen to server and receive message sent from server.
				byte[] buffer = new byte[4096];
				int length = is.read(buffer);
				String messageFromServer;
				try {
					messageFromServer = new String(buffer, 0, length);
				} catch (Exception e) {
					break;
				}
				
				// pass the message received from server to the chain to process.
				this.messageHandlersChain.process(messageFromServer);
				
				// if clientChatThreadOn is set to false, which normally done by one of the messageHandler, this thread will stop listening.
				// Then, the loop will break to avoid socket close exception.
				if(!this.clientChatThreadOn){
					is.close();
					os.close();
					socket.close();
					break;
				}												
			} catch (IOException e) {}					
		}	
	}
	
	/**
	 * {@link #startChatRoomView(Client, Socket)} will be called automatically only after the authorization of login is success.
	 * It will hide the previous login view, and show the chat room view.
	 * @param client
	 * @param socket
	 */
	private void startChatRoomView(Client client, Socket socket){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientChatThread.this.clientChatView = new ClientFrame(client, 
																			ClientChatThread.this, 
																			ClientChatThread.this.clientLoginView.getFrame().getX(),
																			ClientChatThread.this.clientLoginView.getFrame().getY());					
					ClientChatThread.this.clientLoginView.getFrame().setVisible(false);
					ClientChatThread.this.clientChatView.getFrame().setVisible(true);
					ClientChatThread.this.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * {@link #startMessageHandlerChains()} method initiates a ServerMessageHandlersChain, which has a linked list structure.
	 * Different MessageHandlers are registered on this chain as linked nodes.
	 * Different type of messages are handled by different nodes on the chain.
	 * It follows a last-register-first-node model.
	 */
	private void startMessageHandlerChains(){
		this.messageHandlersChain = new ClientMessageHandlersChain(this.clientChatView, this);
		this.messageHandlersChain.register(new ClientSide$UsernameListMessageHandler(this.clientChatView, this));
		this.messageHandlersChain.register(new ClientSide$ServerShutDownMessageHandler(this.clientChatView, this));
		this.messageHandlersChain.register(new ClientSide$ChatContentMessageHandler(this.clientChatView, this));
		this.messageHandlersChain.register(new ClientSide$WhipserMessageHandler(this.clientChatView, this));	
		this.messageHandlersChain.register(new ClientSide$PrivateChatRequestMessageHandler(this.clientChatView, this));
		this.messageHandlersChain.register(new ClientSide$PrivateChatExitMessageHandler(this.clientChatView, this));
		this.messageHandlersChain.register(new ClientSide$PrivateChatContentMessageHandler(this.clientChatView, this));		
	}	
}
