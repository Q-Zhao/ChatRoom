package server.view;

import org.junit.Before;
import org.junit.Test;

import server.ServerLauncher;

public class TestServerFrame {

	ServerFrame serverFrame;
	
	@Before
	public void setUp() {
		serverFrame = new ServerFrame();
	}

	@Test
	public void testStartServer(){
		serverFrame.showServerStartFailure();
		serverFrame.btnNewButton.doClick();
	}
}
