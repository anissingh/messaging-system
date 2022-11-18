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
public class LogInGui extends JPanel implements Responsive {
	
	private static final int IP_FIELD_LEN = 20;
	private static final int PORT_FIELD_LEN = 5;
	private static final int RESPONSE_AREA_X_LEN = 20;
	private static final int RESPONSE_AREA_Y_LEN = 5;
	private static final int IP_LABEL_OFFSET_X = 70;
	private static final int IP_LABEL_OFFSET_Y = 25;
	private static final int NEWLINE_Y_SPACING = 30;
	private final GuiHandler guiHandler;
	private final ClientObserver clientObserver;
	JLabel ipAddressLabel;
	JLabel portLabel;
	JLabel usernameLabel;
	JTextField ipAddressField;
	JTextField portField;
	JTextField usernameField;
	JButton submitButton;
	JTextArea responseTextArea;
	
	public LogInGui(GuiHandler guiHandler, ClientObserver clientObserver, String responseAreaText) {
		this.guiHandler = guiHandler;
		this.clientObserver = clientObserver;
		ipAddressLabel = new JLabel("Enter IP Address of Server:");
		ipAddressField = new JTextField(IP_FIELD_LEN);
		ipAddressField.requestFocus();
		ipAddressLabel.setLabelFor(ipAddressField);
		portLabel = new JLabel("Enter Port of Server:");
		portField = new JTextField(PORT_FIELD_LEN);
		portLabel.setLabelFor(portField);
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new SubmitButtonListener());
		responseTextArea = new JTextArea(RESPONSE_AREA_X_LEN, RESPONSE_AREA_Y_LEN);
		responseTextArea.setLineWrap(true);
		responseTextArea.setText(responseAreaText);
		responseTextArea.setVisible(false);
		responseTextArea.setEditable(false);
		
		this.setLayout(null);
		this.add(ipAddressLabel);
		this.add(ipAddressField);
		this.add(portLabel);
		this.add(portField);
		this.add(submitButton);
		this.add(responseTextArea);
		
		Insets insets = this.getInsets();
		Dimension size = ipAddressLabel.getPreferredSize();
		ipAddressLabel.setBounds(insets.left + IP_LABEL_OFFSET_X, insets.top + IP_LABEL_OFFSET_Y, size.width, size.height);
		size = ipAddressField.getPreferredSize();
		ipAddressField.setBounds(ipAddressLabel.getBounds().x + 160, IP_LABEL_OFFSET_Y - 1, size.width, size.height);
		size = portLabel.getPreferredSize();
		portLabel.setBounds(ipAddressField.getBounds().x + 250, IP_LABEL_OFFSET_Y, size.width, size.height);
		size = portField.getPreferredSize();
		portField.setBounds(portLabel.getBounds().x + 125, IP_LABEL_OFFSET_Y - 1, size.width, size.height);
		size = submitButton.getPreferredSize();
		submitButton.setBounds(GuiFrame.FRAME_WIDTH / 2 - size.width / 2, ipAddressLabel.getBounds().y + NEWLINE_Y_SPACING,
				size.width, size.height);
		size = new Dimension(500, 100);
		responseTextArea.setBounds(GuiFrame.FRAME_WIDTH / 2 - size.width / 2, submitButton.getBounds().y + NEWLINE_Y_SPACING,
				size.width, size.height);
	}
	
	public LogInGui(GuiHandler guiHandler, ClientObserver clientObserver) {
		this(guiHandler, clientObserver, "");
	}
	
	@Override
	public void addToResponse(String message) {
		responseTextArea.append(message);
		responseTextArea.append("\n");
	}
	
	class SubmitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String ipAddress = ipAddressField.getText();
			String port = portField.getText();
			if(ipAddress.equals("") || port.equals("")) {
				addToResponse("Please fill in all required fields");
			} else {
				addToResponse("Connecting to " + ipAddress + ":" + port + "...");
				if(clientObserver.signalCreateSocket(ipAddress, port, LogInGui.this)) {
					addToResponse("Connection successful");
					guiHandler.changeContentPane(PanelType.SET_USERNAME, responseTextArea.getText());
				}
			}
			responseTextArea.setVisible(true);
		}
		
	}
	
}
