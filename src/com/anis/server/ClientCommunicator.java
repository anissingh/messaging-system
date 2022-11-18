package com.anis.server;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientCommunicator implements Runnable {
	
	private Server server;
	private Client client;
	private int id;
	
	public ClientCommunicator(Client client, Server server, int id) {
		this.client = client;
		this.server = server;
		this.id = id;
	}
	
	public void run() {
		BufferedReader reader = client.getReader();
		String messageToRead;
		try {
			// While the client is active
			while((messageToRead = reader.readLine()) != null) {
				if(client.getUsername() == null) {
					client.setUsername(messageToRead);
				} else {
					server.broadcastMesage(messageToRead, client);
				}
			}
			// If it is null, the client closed the connection
			client.closeConnections();
			System.out.println("Closed client " + id + "'s connections.");
			// Tell the server this client is no longer being used
			server.indicateClosureToServer(id);
		} catch (IOException e) {
			 e.printStackTrace();
			System.out.println("Client " + id + "caused IOException.");
		}
	}
	
	
}
