package com.anis.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private static final int PORT = 5000;
	private static final int MAX_CONNECTIONS = 10;
	private static final int BACKLOG = 5;
	private int currConnections;
	private ServerSocket serverSock;
	private Client[] clients;
	
	public Server() {
		currConnections = 0;
		clients = new Client[MAX_CONNECTIONS];
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		// Make sure the server sets up correctly before trying to use it
		if(server.setUpServer()) {
			server.listenForConnections();
		}
	}
	
	// Indicates to the server that a client has closed its socket
	// Returns true if a client has been removed from the clients array, and false if the id given did not
	// represent a client
	public boolean indicateClosureToServer(int id) {
		if(clients[id] != null) {
			String username = clients[id].getUsername();
			clients[id] = null;
			currConnections--;
			System.out.println("Server freed client " + id + "'s memory.");
			broadcastMessage(username + " disconnected.");
			return true;
		} else  {
			System.out.println("Server failed to free client " + id + "'s memory.");
			return false;
		}
	}
	
	// Broadcast a message to all clients connected to the server
	public void broadcastMessage(String message) {
		// Show message in logs
		System.out.println("[Server Message] " + message);
		// Iterate through list of clients
		for(int i = 0; i < clients.length; i++) {
			// Ensure the current client object is not null
			if(clients[i] != null) {
				PrintWriter writer = clients[i].getWriter();
				writer.println(message);
				writer.flush();
			}
		}
	}
	
	// Broadcast a message to all clients connected to the server prefaced with clientSendingMessage's username
	public void broadcastMesage(String message, Client clientSendingMessage) {
		broadcastMessage(clientSendingMessage.getUsername() + ": "+ message);
	}
	
	// Creates and binds a socket
	private ServerSocket createServerSocket(int port, int backlog) {
		try {
			return new ServerSocket(port, backlog);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Server socket could not be created");
			return null;
		}
	}
	
	private PrintWriter createClientWriter(Socket sock) {
		try {
			return new PrintWriter(sock.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to create writer for client.");
			return null;
		}
	}
	
	private BufferedReader createClientReader(Socket sock) {
		try {
			return new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to create reader for client.");
			return null;
		}
	}
	
	private Client initializeClient(Socket sock) {
		// Create new client object with a socket and writer
		// Add the client to the array of clients
		PrintWriter writer = createClientWriter(sock);
		BufferedReader reader = createClientReader(sock);
		if(writer == null || reader == null) {
			return null;
		}
		Client client = new Client(sock, writer, reader);
		// Search client array and find place for client to go
		for(int i = 0; i < clients.length; i++) {
			if(clients[i] == null) {
				clients[i] = client;
				client.setId(i);
				return client;
			}
		}
		// If we get here, the client couldn't be added because there were no null clients in the clients
		// array. This should never happen because our accept call should not accept any incoming connections
		// if we have reached our max connections
		System.out.println("Server error [fatal]: Too many clients in client array.");
		return null;
	}
	
	private boolean setUpServer() {
		// Create and bind socket
		ServerSocket serverSock = createServerSocket(PORT, BACKLOG);
		if(serverSock == null) {
			return false;
		}
		// If socket is successfully created, assign the server socket instance variable
		this.serverSock = serverSock;
		System.out.println("Server set up");
		return true;
	}
	
	private void listenForConnections() {
		// Only listen while we still want to accept connections
		while(currConnections < MAX_CONNECTIONS) {
			try {
				System.out.println("Waiting for client to connect...");
				Socket sock = serverSock.accept();
				Client client = initializeClient(sock);
				if(client != null) {
					currConnections++;
					// Create a new thread object that is used to handle reading from this client and start the thread
					new Thread(new ClientCommunicator(client, this, client.getId())).start();
					System.out.println("Accepted a connection with id " + client.getId() + ". Current connection number : " + currConnections);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failed to accept client.");
			}
		}
	}
	
}
