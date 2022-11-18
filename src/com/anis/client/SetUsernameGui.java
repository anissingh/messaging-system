package com.anis.client;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SetUsernameGui extends JPanel implements Responsive {
	
	private static final int USERNAME_FIELD_LEN = 10;
	private static final int RESPONSE_AREA_X_LEN = 36;
	private static final int RESPONSE_AREA_Y_LEN = 6;
	private static final int USERNAME_LABEL_OFFSET_Y = 25;
	private static final int NEWLINE_Y_SPACING = 30;
	private final GuiHandler guiHandler;
	private final UsernameCommunicator usernameCommunicator;
	private boolean toldToEnterText;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JButton submitButton;
	private JTextArea responseTextArea;
	
	public SetUsernameGui(GuiHandler guiHandler, UsernameCommunicator usernameCommunicator, String responseAreaText) {
		this.guiHandler = guiHandler;
		this.usernameCommunicator = usernameCommunicator;
		this.toldToEnterText = false;
		
		usernameLabel = new JLabel("Enter your username:");
		usernameField = new JTextField(USERNAME_FIELD_LEN);
		usernameLabel.setLabelFor(usernameField);
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new SubmitButtonListener());
		responseTextArea = new JTextArea(RESPONSE_AREA_Y_LEN, RESPONSE_AREA_X_LEN);
		responseTextArea.setLineWrap(true);
		responseTextArea.setText(responseAreaText);
		responseTextArea.setEditable(false);
		
		this.setLayout(null);
		this.add(usernameLabel);
		this.add(usernameField);
		this.add(submitButton);
		this.add(responseTextArea);
		
		Insets insets = this.getInsets();
		Dimension size = usernameLabel.getPreferredSize();
		usernameLabel.setBounds((GuiFrame.FRAME_WIDTH + insets.left + insets.right) / 2 - (size.width + usernameField.getPreferredSize().width) / 2, 
				USERNAME_LABEL_OFFSET_Y, size.width, size.height);
		size = usernameField.getPreferredSize();
		usernameField.setBounds(usernameLabel.getBounds().x + usernameLabel.getPreferredSize().width + 10, usernameLabel.getBounds().y, size.width, size.height);
		size = submitButton.getPreferredSize();
		submitButton.setBounds((GuiFrame.FRAME_WIDTH + insets.left + insets.right) / 2 - size.width / 2, usernameLabel.getBounds().y + NEWLINE_Y_SPACING,
				size.width, size.height);
		size = responseTextArea.getPreferredSize();
		responseTextArea.setBounds((GuiFrame.FRAME_WIDTH + insets.left + insets.right) / 2 - size.width / 2, submitButton.getBounds().y + NEWLINE_Y_SPACING + 10,
				size.width, size.height);
	}
	
	public SetUsernameGui(GuiHandler guiHandler, UsernameCommunicator usernameCommunicator) {
		this(guiHandler, usernameCommunicator, "");
	}

	@Override
	public void addToResponse(String message) {
		responseTextArea.append(message);
		responseTextArea.append("\n");
	}
	
	class SubmitButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String username = usernameField.getText();
			// Only send username to server if it is not blank
			if(!username.isEmpty()) {
				usernameCommunicator.sendUsernameToServer(username);
				addToResponse("Username set to: " + username);
				guiHandler.changeContentPane(PanelType.CHAT, responseTextArea.getText());
			} else if(!toldToEnterText) {
				// Only notify the user to enter a username if they haven't been told before
				addToResponse("Please enter a username.");
				toldToEnterText = true;
			}
		}
		
	}

}
