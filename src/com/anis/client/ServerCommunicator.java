package com.anis.client;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerCommunicator implements Runnable {
	
	private Client client;
	private MessageDisplayer messageDisplayer;
	
	public ServerCommunicator(Client client, MessageDisplayer messageDisplayer) {
		this.client = client;
		this.messageDisplayer = messageDisplayer;
	}

	@Override
	public void run() {
		BufferedReader reader = client.getReader();
		String messageToRead;
		try {
			// Keep reading while nobody has signaled for the thread to stop
			while(!Thread.currentThread().isInterrupted()) {
				if(reader.ready()) {
					messageToRead = reader.readLine();
					messageDisplayer.displayMessage(messageToRead);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Socket closed prematurely. Usually occurres when window is exited improperly.");
		}
	}

}
