package com.anis.client;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class ChatGui extends JPanel implements Responsive, MessageDisplayer {
	
	private static final int CHAT_AREA_X_LEN = 35;
	private static final int CHAT_AREA_Y_LEN = 20;
	private static final int MESSAGE_AREA_X_LEN = 30;
	private static final int RESPONSE_AREA_X_LEN = 36;
	private static final int RESPONSE_AREA_Y_LEN = 6;
	private static final int CHAT_Y_OFFSET = 20;
	private static final int NEWLINE_Y_SPACING = 30;
	private static final int LOGOUT_NEG_X_OFFSET = 72;
	private static final int LOGOUT_NEG_Y_OFFSET = 80;
	private final GuiHandler guiHandler;
	private final ClientObserver clientObserver;
	private JScrollPane chatScroller;
	private JTextArea chatTextArea;
	private JTextField messageTextField;
	private JTextArea responseTextArea;
	private JButton sendButton;
	private JButton logOutButton;
	
	public ChatGui(GuiHandler guiHandler, ClientObserver clientObserver, String responseAreaText) {
		this.guiHandler = guiHandler;
		this.clientObserver = clientObserver;
		
		chatTextArea = new JTextArea(CHAT_AREA_Y_LEN, CHAT_AREA_X_LEN);
		chatTextArea.setLineWrap(true);
		chatTextArea.setEditable(false);
		messageTextField = new JTextField(MESSAGE_AREA_X_LEN);
		messageTextField.addActionListener(new SendMessageListener());
		responseTextArea = new JTextArea(RESPONSE_AREA_Y_LEN, RESPONSE_AREA_X_LEN);
		responseTextArea.setLineWrap(true);
		responseTextArea.setText(responseAreaText);
		responseTextArea.setEditable(false);
		chatScroller = new JScrollPane(chatTextArea);
		chatScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sendButton = new JButton("Send");
		sendButton.addActionListener(new SendMessageListener());
		logOutButton = new JButton("Log Out");
		logOutButton.addActionListener(new LogOutButtonListener());
		
		this.setLayout(null);
		this.add(chatScroller);
		this.add(messageTextField);
		this.add(responseTextArea);
		this.add(sendButton);
		this.add(logOutButton);
		
		Insets insets = this.getInsets();
		Dimension size = chatScroller.getPreferredSize();
		chatScroller.setBounds((GuiFrame.FRAME_WIDTH + insets.left + insets.right) / 2 - size.width / 2, insets.top + CHAT_Y_OFFSET, size.width, size.height);
		size = messageTextField.getPreferredSize();
		messageTextField.setBounds(chatScroller.getBounds().x, chatScroller.getBounds().y + chatScroller.getSize().height, size.width, 
				sendButton.getPreferredSize().height);
		size = sendButton.getPreferredSize();
		sendButton.setBounds(messageTextField.getBounds().x + messageTextField.getSize().width + 5, messageTextField.getBounds().y, size.width, size.height);
		size = responseTextArea.getPreferredSize();
		responseTextArea.setBounds((GuiFrame.FRAME_WIDTH + insets.left + insets.right) / 2 - size.width / 2, messageTextField.getBounds().y + NEWLINE_Y_SPACING,
				size.width, size.height);
		size = logOutButton.getPreferredSize();
		logOutButton.setBounds(GuiFrame.FRAME_WIDTH - insets.right - size.width - LOGOUT_NEG_X_OFFSET, 
				GuiFrame.FRAME_HEIGHT - insets.bottom - size.height - LOGOUT_NEG_Y_OFFSET, size.width, size.height);
		
		clientObserver.beginListeningForMessages(this);
	}
	
	public ChatGui(GuiHandler guiHandler, ClientObserver clientObserver) {
		this(guiHandler, clientObserver, "");
	}

	@Override
	public void addToResponse(String message) {
		responseTextArea.append(message);
		responseTextArea.append("\n");
	}
	
	@Override
	public void displayMessage(String message) {
		chatTextArea.append(message);
		chatTextArea.append("\n");
	}
	
	class SendMessageListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = messageTextField.getText();
			// Only send non-empty messages
			if(!message.isEmpty()) {
				clientObserver.sendMessage(message);
				// Clear text field after message was sent
				messageTextField.setText("");
			}
		}
		
	}
	
	class LogOutButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			addToResponse("Logging out...");
			clientObserver.signalCloseSocket();
			guiHandler.closeWindow();
		}
		
	}

}
