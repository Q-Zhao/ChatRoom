package client;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import client.view.ClientLogin;

/**
 * ClientLauncher is the entry of application for client
 * @author QQZhao
 *
 */

public class ClientLauncher {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLogin loginWindow = new ClientLogin();
					loginWindow.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}

}
