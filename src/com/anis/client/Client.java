package com.anis.client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	private Socket sock;
	private PrintWriter writer;
	private BufferedReader reader;
	
	public Socket getSocket() {
		return sock;
	}
	
	public PrintWriter getWriter() {
		return writer;
	}
	
	public BufferedReader getReader() {
		return reader;
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
	
}
