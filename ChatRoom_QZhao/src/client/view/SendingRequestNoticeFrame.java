package client.view;


import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * SendingRequestNoticeFrame is a GUI showing that a private chat request is under processing. 
 * It will disappear after the remote user either accept or reject the request.
 * @author QQZhao
 *
 */
public class SendingRequestNoticeFrame extends JFrame{
	
	private String remoteUsername;
	private int startPointX;
	private int startPointY;
	
	public SendingRequestNoticeFrame(String remoteUsername, int x, int y) {
		this.startPointX = x;
		this.startPointY = y;
		this.remoteUsername = remoteUsername;
		
		this.setBounds(100, 100, 250, 100);
		this.setLocation(this.startPointX, this.startPointY);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
				
		JLabel lblSendingRequest = new JLabel("Sending Request ...");
		lblSendingRequest.setBounds(60, 10, 128, 16);
		this.getContentPane().add(lblSendingRequest);
		
		String sendingLabelContent = "To: " + this.remoteUsername;
		JLabel lblNewLabel = new JLabel(sendingLabelContent);
		lblNewLabel.setBounds(85, 30, 61, 16);
		getContentPane().add(lblNewLabel);
				
		JLabel lblPleaseWaitFor = new JLabel("Please wait for response");
		lblPleaseWaitFor.setBounds(44, 50, 174, 16);
		this.getContentPane().add(lblPleaseWaitFor);		
	}
}
