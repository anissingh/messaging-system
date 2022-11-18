package com.anis.client;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class GuiFrame implements GuiHandler {
	
	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_HEIGHT = 600;
	private static final String WINDOW_TITLE = "Online Messaging System";
	private final JFrame frame;
	private final ClientObserver clientObserver;
	
	public GuiFrame(ClientObserver clientObserver) {
		frame = new JFrame(WINDOW_TITLE);
		this.clientObserver = clientObserver;
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new LogInGui(this, this.clientObserver));
		frame.setResizable(false);
		frame.setVisible(true);
	}

	@Override
	public void changeContentPane(PanelType panelType, String responseAreaText) {
		if(panelType == PanelType.LOGIN) {
			frame.setContentPane(new LogInGui(this, this.clientObserver, responseAreaText));
		} else if(panelType == PanelType.SET_USERNAME) {
			frame.setContentPane(new SetUsernameGui(this, this.clientObserver, responseAreaText));
		} else {
			// panelType must be PanelType.CHAT
			frame.setContentPane(new ChatGui(this, this.clientObserver, responseAreaText));
		}
	}
	
	@Override
	public void closeWindow() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	
}
