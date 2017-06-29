package client.model;

import client.view.ClientPrivateFrame;

/**
 * PrivateChatRoom represent the private chatting entity between two clients.
 * @author QQZhao
 *
 */

public class PrivateChatRoom {
	
	private String localUsername;
	private String remoteUsername;
	private ClientPrivateFrame privateChatFrame;
	
	public PrivateChatRoom(String localUsername, String remoteUsername) {
		this.localUsername = localUsername;
		this.remoteUsername = remoteUsername;
		Chatroom.getChatroomInstance().addToPrivateChatRoomList(this);
	}

	public String getLocalUsername() {
		return localUsername;
	}

	public String getRemoteUsername() {
		return remoteUsername;
	}

	public ClientPrivateFrame getPrivateChatFrame() {
		return privateChatFrame;
	}

	public void setPrivateChatFrame(ClientPrivateFrame privateChatFrame) {
		this.privateChatFrame = privateChatFrame;
	}

	public boolean isSameAs(String newLocalUsername, String newRemoteUsername){
		if(newLocalUsername.equals(this.localUsername) && newRemoteUsername.equals(this.remoteUsername)){
			return true;
		}
		return false;
	}	
}
