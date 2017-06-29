package client.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.sun.glass.events.KeyEvent;

import client.controller.ClientChatThread;
import client.model.Chatroom;
import client.model.PrivateChatRoom;
import utils.message.MessageParser;
import utils.message.XmlElementName;
import utils.message.XmlElementValue;

import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Font;

public class ClientPrivateFrame extends JFrame{

	private PrivateChatRoom privateChatRoom;
	private ClientChatThread clientChatThread;
	private JLabel chatWithNameLabel;
	private JLabel myNameLabel;
	private int privateChatFrameX;
	private int privateChatFrameY;
	private JTextArea privateChatTypingArea;
	private JTextArea privateChatShowMessageArea;
	
	
	public JTextArea getPrivateChatShowMessageArea() {
		return privateChatShowMessageArea;
	}

	public void updatePrivateChatMessages(String messageFromUsername, String message){		
		this.privateChatShowMessageArea.append(messageFromUsername + ": " + message + "\n");				
	}
		
	public void setChatWithNameLabelText(String remoteUsername) {
		this.chatWithNameLabel.setText(remoteUsername);;
	}
	
	public void setMyNameLabelText(String localUsername) {
		this.myNameLabel.setText(localUsername);;
	}

	public void setPrivateChatRoom(PrivateChatRoom privateChatRoom) {
		this.privateChatRoom = privateChatRoom;
	}

	public ClientPrivateFrame(PrivateChatRoom privateChatRoom, ClientChatThread clientChatThread, int x, int y) {
		this.privateChatRoom = privateChatRoom;
		this.clientChatThread = clientChatThread;
		this.privateChatFrameX = x;
		this.privateChatFrameY = y;
		initialize();
	}

	private void initialize() {
		this.setBounds(100, 100, 350, 300);
		this.setLocation(this.privateChatFrameX, this.privateChatFrameY);
		this.setTitle("Private Chat");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.addWindowListener(new WindowAdapter() {			
			public void windowClosing(WindowEvent event){									
				String privateChatRoomExitMessage = MessageParser.buildMessage(
						XmlElementName.TYPE, XmlElementValue.CLIENT_PRIVATE_EXIT, 
						XmlElementName.FROM_USERNAME, ClientPrivateFrame.this.privateChatRoom.getLocalUsername(),
						XmlElementName.TO_USERNAME, ClientPrivateFrame.this.privateChatRoom.getRemoteUsername());
				ClientPrivateFrame.this.clientChatThread.sendMessage(privateChatRoomExitMessage);
				Chatroom.getChatroomInstance().removeFromPrivateChatRoomList(ClientPrivateFrame.this.privateChatRoom);
				ClientPrivateFrame.this.setVisible(false);
				ClientPrivateFrame.this.dispose();
			}
		});

		JPanel panel = new JPanel();
		panel.setBounds(6, 10, 438, 266);
		this.getContentPane().add(panel);
		panel.setLayout(null);		
		panel.setBorder(BorderFactory.createLineBorder(Color.gray));
		
		JLabel lblNewLabel = new JLabel("With:");
		lblNewLabel.setBounds(10, 6, 43, 22);
		panel.add(lblNewLabel);
		
		chatWithNameLabel = new JLabel("");
		chatWithNameLabel.setBounds(78, 6, 119, 22);
		panel.add(chatWithNameLabel);
		
		JLabel lblMe = new JLabel("Me:");
		lblMe.setBounds(209, 6, 29, 22);
		panel.add(lblMe);
		
		myNameLabel = new JLabel("");
		myNameLabel.setBounds(237, 6, 103, 22);
		panel.add(myNameLabel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 30, 330, 130);
		panel.add(scrollPane_1);
		
		privateChatShowMessageArea = new JTextArea();
		privateChatShowMessageArea.setEditable(false);
		privateChatShowMessageArea.setLineWrap(true);
		privateChatShowMessageArea.setWrapStyleWord(false);
		scrollPane_1.setViewportView(privateChatShowMessageArea);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 193, 330, 60);
		panel.add(scrollPane_2);
		
		privateChatTypingArea = new JTextArea();
		privateChatTypingArea.setLineWrap(true);
		privateChatTypingArea.setWrapStyleWord(false);
		scrollPane_2.setViewportView(privateChatTypingArea);
		privateChatTypingArea.addKeyListener(new KeyListener() {			
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {}			
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {}			
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER){
					privateSendMessagePerformed();
				}
			}
		});
		
		JButton btnSend = new JButton("Send");
		btnSend.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnSend.setBounds(220, 165, 120, 20);
		panel.add(btnSend);
		btnSend.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				privateSendMessagePerformed();				
			}
		});	
		
		JButton btnClear = new JButton("Clear History");
		btnClear.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnClear.setBounds(10, 165, 120, 20);
		panel.add(btnClear);
		
		JLabel lblNewLabel_1 = new JLabel("<Press Ctr+Enter to send a message>");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.ITALIC, 12));
		lblNewLabel_1.setBounds(10, 256, 330, 16);
		panel.add(lblNewLabel_1);
		btnClear.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientPrivateFrame.this.privateChatShowMessageArea.setText("");
			}
		});
	}
	
	public void showRemoteUsernameExit(String remoteUsername){
		JOptionPane.showMessageDialog(this, String.format("%s is no longer in private chat.\nPress OK to exit", remoteUsername), "Exit Private Chat", JOptionPane.OK_OPTION);
		this.setVisible(false);
		this.dispose();
	}
	
	public void privateSendMessagePerformed(){
		if(privateChatTypingArea.getText().trim().equals("")){
			return;
		}						
		String messageSend2Server = MessageParser.buildMessage(XmlElementName.TYPE, XmlElementValue.PRIVATE_CHAT_CONTENT, 
																	XmlElementName.FROM_USERNAME, this.myNameLabel.getText(),
																	XmlElementName.TO_USERNAME, this.chatWithNameLabel.getText(),
																	XmlElementName.MESSAGE, privateChatTypingArea.getText());
		this.clientChatThread.sendMessage(messageSend2Server);
		privateChatTypingArea.setText("");
	}
	
}
