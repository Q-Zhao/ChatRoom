package server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSimulator {
	
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private String responseMessageFromServer;
	
	public ClientSimulator(String targetServerAddress, int targetServerPortNum){
		InetAddress serverHost;
		try {
			serverHost = InetAddress.getByName(targetServerAddress);
			socket = new Socket(serverHost, targetServerPortNum);
			is = socket.getInputStream();
			os = socket.getOutputStream();
				
		} catch (Exception e) {
		}	
	}
	
	public void sendMessage(String message){

		try {
			os.write(message.getBytes());
			byte[] buffer = new byte[4096];
			int length = is.read(buffer);
			this.responseMessageFromServer = new String(buffer, 0, length);			
		} catch (IOException e) {
		}
	}
	
	public String getResponseMessage(){
		return this.responseMessageFromServer;
	}

}
