package client.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSimulator extends Thread{
	
	private ServerSocket serverSocket;
	private String clientSentMessage;
	private String responseMessage;
	
	public ServerSimulator(int portNum){
		try {
			this.serverSocket = new ServerSocket(portNum);
		} catch (IOException e) {
		}
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}



	public void run(){
		
		while(true){
			try {
				Socket socket = this.serverSocket.accept();
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				byte[] buffer = new byte[4096];
				int length = is.read(buffer);				
				clientSentMessage = new String(buffer, 0, length);
				
				if(this.responseMessage.equals("stop")){
					break;
				}

				os.write(this.responseMessage.getBytes());
				
			} catch (IOException e) {
			}
		}

	}

	public String getClientSentMessage() {
		return clientSentMessage;
	}
}
