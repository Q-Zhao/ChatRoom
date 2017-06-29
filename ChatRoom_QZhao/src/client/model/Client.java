package client.model;

/**
 * Client class is an entity object, representing a specific client user.
 * @author QQZhao
 *
 */
public class Client {

	private String userName;
	
	public Client(String userName){
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
