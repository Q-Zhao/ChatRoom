package server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ServerContainer is singleton entity model of server.
 * @author QQZhao
 *
 */
public class ServerContainer {
	
	private List<User> currentUserList;
	private List<String> currentUserNameList;
	private static ServerContainer serverSingleton;
	
	private ServerContainer(){
		currentUserList = new ArrayList<>();
		currentUserNameList = new ArrayList<>();
	}
	
	/**
	 * {@link #getServerInstance()} guarantees only one instance of ServerContainer can be generated.
	 * @return the singleton instance of ServerContainser.
	 */
	public static synchronized ServerContainer getServerInstance(){
		if(serverSingleton == null){
			serverSingleton = new ServerContainer();
		}
		return serverSingleton;
	}

	/**
	 * {@link #isValidUserName(String)} checks if a user input username is valid
	 * @param userName
	 * @return true if the username is not null, not empty string, and not currently already exists.
	 */
	public boolean isValidUserName(String userName){
		return userName != null &&
				!userName.equals("") &&
				!isUserNameExist(userName);
	}
	
	/**
	 * {@link #isUserNameExist(String)} checks if a username already exists.
	 * @param userName
	 * @return true if this username already exists. 
	 * @return false otherwise.
	 */
	public boolean isUserNameExist(String userName){
		for (User user : currentUserList){
			if(userName.equals(user.getUserName())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * {@link #registerUser(User)} add the newly formed user object to the server maintained list.
	 * @param newUser
	 */
	public void registerUser(User newUser){
		if(isUserNameExist(newUser.getUserName())){
			return;
		}
		this.currentUserList.add(newUser);
		this.currentUserNameList.add(newUser.getUserName());
	}

	public List<String> getCurrentUserNameList() {
		return currentUserNameList;
	}
	
	/**
	 * {@link #sendEveryUserMessage(String)} loops each user server maintains, and send the message via each user's socket.
	 * @param message
	 */
	public void sendEveryUserMessage(String message){
		try{
			for (User eachUser : this.currentUserList){
				eachUser.getServerChatThread().sendMessage(message);
			}
		}catch(Exception e){}		
	}
	
	/**
	 * {@link #sendMessageToOneClient(String, String)} send a message to a specific user by identifing the username.
	 * @param usernameSendTo
	 * @param message
	 */
	public void sendMessageToOneClient(String usernameSendTo, String message){
		for (User user : currentUserList){
			if (user.getUserName().equals(usernameSendTo)){	
				user.getServerChatThread().sendMessage(message);
				break;
			}
		}
	}
	
	/**
	 * {@link #removeUserByUsername(String)} removes a specific user by identifing username after this user quit the chatroom.
	 * @param username
	 */
	public void removeUserByUsername(String username){
		for (User user : currentUserList){
			if (user.getUserName().equals(username)){	
				currentUserList.remove(user);
				break;
			}
		}	
		for (String existingUsername : currentUserNameList){
			if (existingUsername.equals(username)){
				currentUserNameList.remove(existingUsername);
				break;
			}
		}
	}
	
	/**
	 * {@link #converUserListPrintable()} converts the username ArrayList to a neat printable string with break line between usernames.
	 * @return
	 */
	public String converUserListPrintable(){		
		StringBuilder sb = new StringBuilder();
		for(String username : currentUserNameList){
			sb.append(username).append("\n");
		}		
		return sb.toString().trim();		
	}
	
	public void clearAllUsers(){
		this.currentUserNameList.clear();
		this.currentUserList.clear();
	}

}
