package client.controller;

import utils.validator.StringValidator;

/**
 * UserInformationValidator is used to determine if the client's input in login dialog is valid or not.
 * @author QQZhao
 *
 */

public class UserInformationValidator {
	
	private String userName;
	private String serverAddress;
	private String port;
	
	public UserInformationValidator(String userName, String serverAddress, String port){
		super();
		this.userName = userName;
		this.serverAddress = serverAddress;
		this.port = port;
	}
	
	public boolean isUserNameInputNotEmpty(){
		return StringValidator.isNotEmpty(userName);
	}
	
	public boolean isServerInfoInputNotEmpty(){
		return (StringValidator.isNotEmpty(serverAddress) && 
				StringValidator.isNotEmpty(port));	
	}
	
	public boolean isServerPortInputValid(){
		return StringValidator.isNumber(port) && StringValidator.isBetweenRange(port, 1024, 65535);
	}
}
