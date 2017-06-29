package client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Chatroom is an entity class idenfitying the concept of chatroom.
 * It generates singleton instance since each user can only join in one chatroom.
 * @author QQZhao
 *
 */
public class Chatroom {

	private List<String> usernameList;
	private static Chatroom chatroomSingleton;
	private List<PrivateChatRoom> privateChatRoomList;
	private Client client;
	
	private Chatroom(){
		this.usernameList = new ArrayList<>();
		this.privateChatRoomList = new ArrayList<>();
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public static synchronized Chatroom getChatroomInstance(){
		if(chatroomSingleton == null){
			chatroomSingleton = new Chatroom();
		}
		return chatroomSingleton;
	}

	public List<String> getUsernameList() {
		return usernameList;
	}

	public void setUsernameList(List<String> usernameList) {
		this.usernameList = usernameList;
	}
	
	/**
	 * {@link #converUsernameList2Array()} convert the arrayList of usernames to the corresponding array, for the purpose of JList component in JFrame.
	 * @return the array of username strings
	 */
	public String[] converUsernameList2Array(){
		String[] usernameArray = new String[this.usernameList.size()];
		usernameArray = this.usernameList.toArray(usernameArray);
		for (int i = 0; i < usernameArray.length; i++){
			if(usernameArray[i].equals(this.client.getUserName())){
				usernameArray[i] = (this.client.getUserName() + " (self)");
			}
		}
		return usernameArray;
	}
	
	
	public boolean thisPrivateChatRoomExists(String newLocalUsername, String newRemoteUsername){
		for(PrivateChatRoom pcr : privateChatRoomList){
			if(pcr.isSameAs(newLocalUsername, newRemoteUsername)){
				return true;
			}
		}
		return false;
	}
	
	public PrivateChatRoom getThisPrivateChatRoom(String localUsername, String remoteUsername){
		for(PrivateChatRoom pcr : privateChatRoomList){
			if(pcr.isSameAs(localUsername, remoteUsername)){
				return pcr;
			}
		}
		return null;
	}
	
	public void addToPrivateChatRoomList(PrivateChatRoom pcr){
		this.privateChatRoomList.add(pcr);
	}
	
	public void removeFromPrivateChatRoomList(PrivateChatRoom pcr){
		this.privateChatRoomList.remove(pcr);		
	}
	
	public boolean hasPrivateChat(){
		return this.privateChatRoomList.size() != 0;
	}

	public List<PrivateChatRoom> getPrivateChatRoomList() {
		return privateChatRoomList;
	}
	
}
