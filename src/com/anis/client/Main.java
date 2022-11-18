package com.anis.client;

public class Main {
	
	public static void main(String[] args) {
		Client client = new Client();
		ClientObserver clientObserver = new ClientManager(client);
		new GuiFrame(clientObserver);
	}
	
}
