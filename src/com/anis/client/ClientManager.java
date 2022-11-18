package com.anis.client;

import java.io.IOException;
import java.io.PrintWriter;

public class ClientManager implements ClientObserver {
	
	private Client client;
	private Thread listeningThread;
	
	public ClientManager(Client client) {
		this.client = client;
	}
	
	public ClientManager() {
		this(new Client());
	}

	@Override
	public boolean signalCreateSocket(String ipAddress, String portStr, Responsive currGuiPanel) {
		return ClientSocketCreator.initializeClientConnection(ipAddress, portStr, currGuiPanel, client);
	}
	

	@Override
	public void beginListeningForMessages(MessageDisplayer messageDisplayer) {
		listeningThread = new Thread(new ServerCommunicator(client, messageDisplayer));
		listeningThread.start();
	}
	
	@Override
	public void sendMessage(String message) {
		PrintWriter writer = client.getWriter();
		writer.println(message);
		writer.flush();
	}

	@Override
	public void signalCloseSocket() {
		// Signal for the listening thread to stop
		listeningThread.interrupt();
		// Wait for listening thread to stop reading before closing sockets
		try {
			listeningThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Close sockets
		client.getWriter().close();
		try {
			client.getReader().close();
			client.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to close client connections.");
		}
		
	}

	@Override
	public void sendUsernameToServer(String username) {
		sendMessage(username);
	}


}
