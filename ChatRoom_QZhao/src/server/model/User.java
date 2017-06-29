package server.model;

import server.controller.ServerChatThread;

/**
 * User class is an entity object identifying a specific user.
 * For each user, a username and the correponding ServerChatThread is recorded.
 * @author QQZhao
 *
 */
public class User {
	
	private String userName;
	private ServerChatThread serverChatThread;
	
	public User(String username){
		this.userName = username;
	}
	
	public User(String userName, ServerChatThread serverChatThread){
		this.userName = userName;
		this.serverChatThread = serverChatThread;
		this.serverChatThread.setBelongsToUser(this);
	}
		
	public ServerChatThread getServerChatThread() {
			return serverChatThread;
		}

	public String getUserName() {
		return userName;
	}
}
