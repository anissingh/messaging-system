package com.anis.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	private String username;
	private Socket sock;
	private PrintWriter writer;
	private BufferedReader reader;
	private int id;
	
	public Client(Socket sock, PrintWriter writer, BufferedReader reader) {
		this.sock = sock;
		this.writer = writer;
		this.reader = reader;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean closeConnections() {
		try {
			reader.close();
			writer.close();
			sock.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Socket getSocket() {
		return sock;
	}
	
	public PrintWriter getWriter() {
		return writer;
	}
	
	public BufferedReader getReader() {
		return reader;
	}
	
	public int getId() {
		return id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setSocket(Socket sock) {
		this.sock = sock;
	}
	
	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}
	
	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
}
