package client.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import client.controller.ClientChatThread;
import client.model.Chatroom;
import client.model.Client;
import client.model.PrivateChatRoom;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import com.sun.glass.events.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;

/**
 * ClientFrame is a class generated by WindowBuilder plugins.
 * It is the main view for user to communicate with other users.
 * @author QQZhao
 *
 */
public class ClientFrame{

	private JFrame frame;
	private Map<String, JFrame> sendingPrivateRequestFramesMap;
	private Client client;
	private ClientChatThread clientChatThread;	
	private JList<String> showUserNameTextList;
	private JTextArea showMsgTextArea;
	private JTextArea sendMsgTextArea;
	private JTextArea showChatRoomStatusTextArea;
	private JButton sendMsgBtn;
	private JButton saveChattingBtn;
	private JButton clearHistoryBtn;
	private JButton clearChatRoomStatusBtn;
	private JScrollPane showMsgScrollPane;
	private String chosenUsername;	
	private final String WISPER_TO_FORMAT = "[whisper to %s]: ";
	private final String WISPER_FROM_FORMAT = "[%s whispered to you]: ";
	private int startingPointX;
	private int startingPointY;
	
	
	public ClientFrame(){
		initialize();
	}	
	
	public ClientFrame(Client client, ClientChatThread clientChatThread, int x, int y) {
		this.client = client;
		this.clientChatThread = clientChatThread;
		sendingPrivateRequestFramesMap = new HashMap<>();
		this.startingPointX = x;
		this.startingPointY = y;
		initialize();
	}
	
	/**
	 * initialize the GUI.
	 */	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 655, 505);
		frame.setLocation(this.startingPointX, this.startingPointY);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setAlwaysOnTop(true);		
		frame.addWindowListener(new WindowAdapter() {			
			public void windowClosing(WindowEvent event){													
				// handling private chat first, send exit information to each single private chatroom.
				// server just forward these message to the corresponding remoteUser.
				if(Chatroom.getChatroomInstance().hasPrivateChat()){					
					List<PrivateChatRoom> privateChatroomList = Chatroom.getChatroomInstance().getPrivateChatRoomList();
					for (PrivateChatRoom pcr : privateChatroomList){
						String privateChatRoomExitMessage = MessageParser.buildMessage(
																XmlElementName.TYPE, XmlElementValue.CLIENT_PRIVATE_EXIT, 
																XmlElementName.FROM_USERNAME, pcr.getLocalUsername(),
																XmlElementName.TO_USERNAME, pcr.getRemoteUsername());
						ClientFrame.this.clientChatThread.sendMessage(privateChatRoomExitMessage);
						// let thread sleep for 0.1 seconds to avoid sending two message at same time.
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {}
					}					
				}
				// let thread sleep for 0.1 seconds to avoid sending two message at same time.
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				
				// handling user's exist in chatroom by sending server information.
				String clientExitMessage = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.CLIENT_EXIT, 
																		XmlElementName.USERNAME, ClientFrame.this.client.getUserName());
				ClientFrame.this.clientChatThread.sendMessage(clientExitMessage);
				
			}
		});
		
		// Panels containing static information on the top of the frame.
		JPanel staticPanel = new JPanel();
		staticPanel.setBounds(5, 5, 640, 35);	
		frame.getContentPane().add(staticPanel);
		staticPanel.setLayout(null);
		
		JLabel staticLabel = new JLabel("Chat Room");
		staticLabel.setForeground(Color.RED);
		staticLabel.setFont(new Font("Lithos Pro", Font.PLAIN, 20));
		staticLabel.setBounds(237, 6, 136, 29);
		staticPanel.add(staticLabel);
		
		JLabel staticUsernamelabel = new JLabel("My Name:");
		staticUsernamelabel.setForeground(Color.BLUE);
		staticUsernamelabel.setBounds(498, 0, 136, 18);
		staticPanel.add(staticUsernamelabel);		
		JLabel dynamicUsernameLabel = new JLabel(this.client.getUserName());
		dynamicUsernameLabel.setForeground(Color.BLUE);
		dynamicUsernameLabel.setBounds(498, 17, 136, 18);
		staticPanel.add(dynamicUsernameLabel);
		
		// panel to show username list
		JPanel clientListPanel = new JPanel();
		clientListPanel.setBounds(5, 40, 200, 290);
		clientListPanel.setBorder(BorderFactory.createTitledBorder("Current Users:"));
		frame.getContentPane().add(clientListPanel);
		clientListPanel.setLayout(null);
		
		JScrollPane showUserNameScrollPane = new JScrollPane();
		showUserNameScrollPane.setBounds(5, 20, 185, 220);
		clientListPanel.add(showUserNameScrollPane);		
		
		showUserNameTextList = new JList<>();
		showUserNameTextList.setBounds(5, 20, 230, 220);
		showUserNameScrollPane.setViewportView(showUserNameTextList);
		showUserNameTextList.addMouseListener(new MouseAdapter() {
			
			final RequestPopup pop = new RequestPopup(ClientFrame.this);			
			public void mouseClicked(MouseEvent e){
		        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2){
		        	@SuppressWarnings("unchecked")
					int index = ((JList<String>)e.getSource()).locationToIndex(e.getPoint());
		        	ClientFrame.this.chosenUsername = Chatroom.getChatroomInstance().getUsernameList().get(index);
		        	if (ClientFrame.this.chosenUsername.equals(ClientFrame.this.client.getUserName())){
		        		return;
		        	}
		        	sendMsgTextArea.setText(String.format(WISPER_TO_FORMAT, ClientFrame.this.chosenUsername));
		        }
		        if(SwingUtilities.isRightMouseButton(e)){
		        	int index = ((JList<String>)e.getSource()).locationToIndex(e.getPoint());
		        	String selectedUsernameToRequest = Chatroom.getChatroomInstance().getUsernameList().get(index);
		        	if(!selectedUsernameToRequest.equals(ClientFrame.this.client.getUserName())){
		        		pop.setPopConfiguration(ClientFrame.this.client.getUserName(), selectedUsernameToRequest);
		        		pop.show(e.getComponent(), e.getX(), e.getY());
		        	}		        	
		        }
			}
		});
		
		/*
		 * show tips
		 */		
		String staticMessage = "<html><p>Double-click a user to wisper.</p></html>";
		JLabel TipsContent = new JLabel(staticMessage);
		TipsContent.setFont(new Font("Lucida Grande", Font.ITALIC, 11));
		TipsContent.setBounds(5, 241, 188, 16);
		clientListPanel.add(TipsContent);		
		String staticMessage2 = "<html><p>Right-click a user to start a private <br>chat room.</p></html>";
		JLabel TipsContent2 = new JLabel(staticMessage2);
		TipsContent2.setFont(new Font("Lucida Grande", Font.ITALIC, 11));
		TipsContent2.setBounds(5, 257, 188, 31);
		clientListPanel.add(TipsContent2);
		
		/*
		 * show server status panel.
		 */
		JPanel chatRoomStatusPanel = new JPanel();
		chatRoomStatusPanel.setBounds(5, 330, 200, 145);
		chatRoomStatusPanel.setBorder(BorderFactory.createTitledBorder("Chat Room Status:"));
		frame.getContentPane().add(chatRoomStatusPanel);
		chatRoomStatusPanel.setLayout(null);
		
		JScrollPane chatRoomStatusScrollPane = new JScrollPane();
		chatRoomStatusScrollPane.setBounds(5, 20, 185, 115);
		chatRoomStatusPanel.add(chatRoomStatusScrollPane);	
		
		showChatRoomStatusTextArea = new JTextArea();
		showChatRoomStatusTextArea.setEditable(false);
		showChatRoomStatusTextArea.setBounds(5, 5, 200, 160);
		chatRoomStatusScrollPane.setViewportView(showChatRoomStatusTextArea);
		
		clearChatRoomStatusBtn = new JButton("Clear");
		clearChatRoomStatusBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		clearChatRoomStatusBtn.setBounds(130, 0, 60, 19);
		chatRoomStatusPanel.add(clearChatRoomStatusBtn);
		clearChatRoomStatusBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientFrame.this.showChatRoomStatusTextArea.setText("");				
			}
		});
			
		/*
		 * show chatting message panel
		 */
		JPanel allMessagePanel = new JPanel();
		allMessagePanel.setBounds(210, 40, 440, 290);
		allMessagePanel.setBorder(BorderFactory.createTitledBorder("Messages:"));
		frame.getContentPane().add(allMessagePanel);
		allMessagePanel.setLayout(null);
				
		showMsgScrollPane = new JScrollPane();
		showMsgScrollPane.setBounds(10, 20, 420, 260);
		allMessagePanel.add(showMsgScrollPane);
		
		showMsgTextArea = new JTextArea();
		showMsgTextArea.setLineWrap(true);
		showMsgTextArea.setWrapStyleWord(false);
		showMsgTextArea.setEditable(false);
		showMsgTextArea.setBounds(5, 5, 400, 260);
		showMsgScrollPane.setViewportView(showMsgTextArea);
		
		/*
		 * show user input message
		 */
		JPanel clientTextPanel = new JPanel();
		clientTextPanel.setBounds(210, 330, 440, 145);
		clientTextPanel.setBorder(BorderFactory.createTitledBorder("Send Messages:"));
		frame.getContentPane().add(clientTextPanel);
		clientTextPanel.setLayout(null);
		
		JScrollPane sendMsgScrollPane = new JScrollPane();
		sendMsgScrollPane.setBounds(10, 20, 315, 95);
		clientTextPanel.add(sendMsgScrollPane);
		
		sendMsgTextArea = new JTextArea();
		sendMsgTextArea.setEditable(true);
		sendMsgTextArea.setLineWrap(true);
		sendMsgTextArea.setWrapStyleWord(false);
		sendMsgScrollPane.setViewportView(sendMsgTextArea);
		sendMsgTextArea.addKeyListener(new KeyListener() {			
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {}			
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {}			
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER){
					sendMessageAction();
				}
			}
		});
				
		sendMsgBtn = new JButton("Send");
		sendMsgBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		sendMsgBtn.setBounds(325, 20, 105, 30);		
		clientTextPanel.add(sendMsgBtn);
		sendMsgBtn.addActionListener(new ActionListener() {						
			@Override
			public void actionPerformed(ActionEvent e) {				
				sendMessageAction();
			}
		});
						
		clearHistoryBtn = new JButton("Clear History");
		clearHistoryBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		clearHistoryBtn.setBounds(325, 52, 105, 30);
		clientTextPanel.add(clearHistoryBtn);
		clearHistoryBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientFrame.this.showMsgTextArea.setText("");				
			}
		});
		
		JLabel lblNewLabel = new JLabel("<Press Ctr+Enter to send a message>");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.ITALIC, 12));
		lblNewLabel.setBounds(10, 118, 325, 16);
		clientTextPanel.add(lblNewLabel);
		
		saveChattingBtn = new JButton("Save Chat");
		saveChattingBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		saveChattingBtn.setBounds(325, 84, 105, 30);
		clientTextPanel.add(saveChattingBtn);		
		saveChattingBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {				
				try{
					JFileChooser fileChooser = new JFileChooser();
				    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);				
					fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter(){
						@Override
						public boolean accept(File file) {
							return file.isDirectory() || file.getName().endsWith(".txt");
						}
						@Override
						public String getDescription() {
							return "";
						}					
					});
					
					fileChooser.showSaveDialog(saveChattingBtn);
					String savedFile = fileChooser.getSelectedFile().getAbsolutePath().endsWith("txt")? 
							fileChooser.getSelectedFile().getAbsolutePath()
							: fileChooser.getSelectedFile().getAbsolutePath() + ".txt";
	
					File file = new File(savedFile);
					try {						
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath()));
						bos.write(showMsgTextArea.getText().getBytes());
						bos.close();
						bos.flush();
						JOptionPane.showMessageDialog(frame, "Log Saved Successfully");
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(frame, "Can Not Save\nCheck Permission", "Save Failture", JOptionPane.ERROR_MESSAGE);
				}
				}catch (Exception e2){
					return;
				}
			}
		});

	}
		
	public JList<String> getShowUserNameTextList() {
		return showUserNameTextList;
	}

	public void refreshUsernameListView(String[] usernameArray){
		this.showUserNameTextList.setListData(usernameArray);
	}
	
	public void AddToChatRoomStatusTextArea(String newStatusInfo){
		this.showChatRoomStatusTextArea.append(newStatusInfo);
		this.showChatRoomStatusTextArea.append("\n");
	}
	
	public void showCanNotHandleMessageError(){
		JOptionPane.showMessageDialog(frame, "Unexpected Error", "Error", JOptionPane.ERROR_MESSAGE);
		return;
	}
	
	public void showShutDown(){
		JOptionPane.showMessageDialog(frame, "Server is shutdown.\nPress OK to exit chatting", "Server Shut Down", JOptionPane.OK_OPTION);
		System.exit(0);
	}
	
	public void updateChatMessages(String messageFromUsername, String message){		
		this.showMsgTextArea.append(messageFromUsername + ": " + message + "\n");				
	}
		
	public JTextArea getShowMsgTextArea() {
		return showMsgTextArea;
	}

	public void showWisperMessages(String messageFromUsername, String messageToUsername, String message){
		
		if (messageFromUsername.equals(ClientFrame.this.client.getUserName())){
			this.showMsgTextArea.append(String.format(WISPER_TO_FORMAT + message + "\n", messageToUsername));	
		}else{
			this.showMsgTextArea.append(String.format(WISPER_FROM_FORMAT + message + "\n", messageFromUsername));
		}						
	}
	
	private boolean isWisperMessage(String message){
		return message.startsWith(String.format(WISPER_TO_FORMAT, ClientFrame.this.chosenUsername));
	}
	
	/**
	 * {@link #sendMessageAction()}} sends the user input message to server based on type.
	 */
	private void sendMessageAction() {
		if(sendMsgTextArea.getText().trim().equals("")){
			return;
		}				
		String userTypedMessage = sendMsgTextArea.getText();
		String messageSend2Server = "";
		if(isWisperMessage(userTypedMessage)){
			messageSend2Server = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.WHISPER_MESSAGE, 
				XmlElementName.FROM_USERNAME, ClientFrame.this.client.getUserName(),
				XmlElementName.TO_USERNAME, ClientFrame.this.chosenUsername, 
				XmlElementName.MESSAGE, userTypedMessage.substring(userTypedMessage.indexOf(String.format(WISPER_TO_FORMAT, ClientFrame.this.chosenUsername)) + String.format(WISPER_TO_FORMAT, ClientFrame.this.chosenUsername).length()));
		}else{
			messageSend2Server = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.CHAT_CONTENT, 
																XmlElementName.USERNAME, ClientFrame.this.client.getUserName(), 
																XmlElementName.MESSAGE, userTypedMessage);
		}
		ClientFrame.this.clientChatThread.sendMessage(messageSend2Server);
		sendMsgTextArea.setText("");
	}
	
	/**
	 * {@link #showReceivingPrivateChatRequest(String)} show a dialog ask receiving a private chat request from another remote user.
	 * A dialog will be shown to the user, asking for permission.
	 * @param remoteUsername
	 */
	public void showReceivingPrivateChatRequest(String remoteUsername){
		
		int selection = JOptionPane.showConfirmDialog(frame, 
													String.format("%s wants to private chat with you.\nDo you want to accept?", remoteUsername), 
													"Private Chat Request", 
													JOptionPane.YES_NO_OPTION);
		if(selection == JOptionPane.YES_OPTION){
			String privateChatResponseMessage = MessageParser.buildMessage(
					XmlElementName.TYPE, XmlElementValue.CLIENT_PRIVATE_RESPONSE, 
					XmlElementName.FROM_USERNAME, ClientFrame.this.client.getUserName(),
					XmlElementName.TO_USERNAME, remoteUsername,
					XmlElementName.MESSAGE, XmlElementValue.ACCEPT);
			ClientFrame.this.clientChatThread.sendMessage(privateChatResponseMessage);
			PrivateChatRoom privateChatRoom = new PrivateChatRoom(ClientFrame.this.client.getUserName(), remoteUsername);
			ClientPrivateFrame privateChatFrame = new ClientPrivateFrame(privateChatRoom, 
																		clientChatThread, 
																		this.getFrame().getX() + this.getFrame().getWidth(), 
																		this.getFrame().getY());
			privateChatFrame.setChatWithNameLabelText(remoteUsername);
			privateChatFrame.setMyNameLabelText(ClientFrame.this.client.getUserName());
			privateChatRoom.setPrivateChatFrame(privateChatFrame);
			privateChatFrame.setVisible(true);
		}else{
			String privateChatResponseMessage = MessageParser.buildMessage(
					XmlElementName.TYPE, XmlElementValue.CLIENT_PRIVATE_RESPONSE, 
					XmlElementName.FROM_USERNAME, ClientFrame.this.client.getUserName(),
					XmlElementName.TO_USERNAME, remoteUsername,
					XmlElementName.MESSAGE, XmlElementValue.REJECT);
			ClientFrame.this.clientChatThread.sendMessage(privateChatResponseMessage);
		}
	}
	
	/**
	 * {@link #showReceivingRejectiononPrivateChatRequest(String)}} shows a dialog that a private chat request send ealier was rejected by a remote user.
	 * @param remoteUsername
	 */
	public void showReceivingRejectiononPrivateChatRequest(String remoteUsername){
		JOptionPane.showMessageDialog(frame, String.format("Your request to chat with %s was rejected.", remoteUsername), "Rejection Message", JOptionPane.OK_OPTION);
	}
	

	public JFrame getFrame() {
		return frame;
	}
	
	public String getChosenUsername() {
		return chosenUsername;
	}
	
	public ClientChatThread getClientChatThread() {
		return clientChatThread;
	}
	
	public Map<String, JFrame> getSendingPrivateRequestFramesMap() {
		return sendingPrivateRequestFramesMap;
	}
	
	public Client getClient() {
		return client;
	}

	public JButton getClearHistoryBtn() {
		return clearHistoryBtn;
	}

	public JButton getClearChatRoomStatusBtn() {
		return clearChatRoomStatusBtn;
	}

	public JTextArea getShowChatRoomStatusTextArea() {
		return showChatRoomStatusTextArea;
	}
}

/**
 * RequestPopup is a class handling sending private chat request.
 * It is initiated after user right-click a remote user in username list.
 * @author QQZhao
 *
 */
class RequestPopup extends JPopupMenu{
	
	private String remoteUsername;
	private String localUsername;
	private JMenuItem requestPrivateChatMenu;

	public void setPopConfiguration(String localUsername, String remoteUsername) {
		this.localUsername = localUsername;
		this.remoteUsername = remoteUsername;
	}
	
	public JMenuItem getRequestPrivateChatMenu() {
		return requestPrivateChatMenu;
	}

	public RequestPopup(ClientFrame clientFrame){
		
		requestPrivateChatMenu = new JMenuItem("Request Private Chat..");
		requestPrivateChatMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// If the requested private chatroom does not exist, it will create a new one.
				if(!Chatroom.getChatroomInstance().thisPrivateChatRoomExists
						(RequestPopup.this.localUsername, RequestPopup.this.remoteUsername)){
					
					String privateChatRoomRequestMessage = MessageParser.buildMessage(
							XmlElementName.TYPE, XmlElementValue.CLIENT_PRIVATE_REQUEST, 
							XmlElementName.FROM_USERNAME, RequestPopup.this.localUsername,
							XmlElementName.TO_USERNAME, RequestPopup.this.remoteUsername);
					// sending private request message to server.
					clientFrame.getClientChatThread().sendMessage(privateChatRoomRequestMessage);
					// initiate a small view, indicating the sending is under processing.
					SendingRequestNoticeFrame srnf = new SendingRequestNoticeFrame(RequestPopup.this.remoteUsername, 
																			clientFrame.getFrame().getX() + clientFrame.getFrame().getWidth(), 
																			clientFrame.getFrame().getY());
					clientFrame.getSendingPrivateRequestFramesMap().put(RequestPopup.this.remoteUsername, srnf);
					srnf.setVisible(true);

				}
				// If the requested private chat room already exists, it will simply show it and bring to front.
				else{
					PrivateChatRoom privateChatRoom = Chatroom.getChatroomInstance().getThisPrivateChatRoom(RequestPopup.this.localUsername, RequestPopup.this.remoteUsername);
					privateChatRoom.getPrivateChatFrame().toFront();
					privateChatRoom.getPrivateChatFrame().requestFocus();
				}
				
			}
		});		
		this.add(requestPrivateChatMenu);
	}
}