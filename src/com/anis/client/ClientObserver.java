package com.anis.client;

public interface ClientObserver extends UsernameCommunicator {
	
	boolean signalCreateSocket(String ipAddress, String portStr, Responsive currGuiPanel);
	void beginListeningForMessages(MessageDisplayer messageDisplayer);
	void sendMessage(String message);
	void signalCloseSocket();
	
}
