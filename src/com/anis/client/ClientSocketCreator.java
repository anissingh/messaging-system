package com.anis.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketCreator {
	
	public static Socket createSocket(String ipAddress, String portStr, Responsive currGuiPanel) {
		boolean invalidPort = false;
		int port = 0;
		try {
			port = Integer.parseInt(portStr);
			if(port < 0 || port > 65535) {
				invalidPort = true;
			}
		} catch (NumberFormatException e) {
			invalidPort = true;
		}
		
		if(invalidPort) {
			currGuiPanel.addToResponse("Invalid port entered.");
			return null;
		} 
		
		try {
			return new Socket(ipAddress, port);
		} catch (IOException e) {
			e.printStackTrace();
			currGuiPanel.addToResponse("Connection could not be established.");
			return null;
		}
		
	}
	
	public static PrintWriter createWriterStream(Socket socket, Responsive currGuiPanel) {
		try {
			return new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			currGuiPanel.addToResponse("Connection could not be established.");
			return null;
		}
	}
	
	public static BufferedReader createReaderStream(Socket socket, Responsive currGuiPanel) {
		try {
			return new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			currGuiPanel.addToResponse("Connection could not be established.");
			return null;
		}
	}
	
	// Returns false if connection is not established for any reason. Otherwise, returns true
	public static boolean initializeClientConnection(String ipAddress, String portStr, Responsive currGuiPanel, Client client) {
		Socket socket = createSocket(ipAddress, portStr, currGuiPanel);
		if(socket == null) {
			return false;
		}
		PrintWriter writer = createWriterStream(socket, currGuiPanel);
		BufferedReader reader = createReaderStream(socket, currGuiPanel);
		if(writer == null || reader == null) {
			return false;
		} else {
			// Only assign client values if all the above code runs properly
			client.setSocket(socket);
			client.setWriter(writer);
			client.setReader(reader);
			return true;
		}
	}
	
}
