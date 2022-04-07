package com.server;

import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.io.*;

public class ServerSide {
	ServerSocket server;

	static AtomicInteger clientId = new AtomicInteger(0);

	public static void main(String args[]) {
		try {
			ServerSocket server = new ServerSocket(5230);
			System.out.println("Server started at port 5230");
			System.out.println("Waiting for a client ...");
			DataInputStream consoleIn = new DataInputStream(System.in);
			Socket clientSock = null;
			while (true) {
				clientSock = server.accept();
				System.out.println("Client accepted");

				new ClientThread(clientSock, clientId.addAndGet(1)).start();
			}
		} catch (IOException i) {
			System.out.println(i);
		}
	}

	public static class ClientThread extends Thread {
		Socket clientSock;
		int myId;

		public ClientThread(Socket clientSock, int myId) {
			this.clientSock = clientSock;
			this.myId = myId;
		}

		@Override
		public void run() {
			handleClient(clientSock, myId);
		}
	}

	private static void handleClient(Socket clientSock, int myId) {
		PrintWriter clntOut = null;
		DataInputStream clntIn = null;
		try {
			clntOut = new PrintWriter(new DataOutputStream(clientSock.getOutputStream()));
			clntIn = new DataInputStream(clientSock.getInputStream());
			String line = " ";
			boolean isClientOpen = true;
			while (isClientOpen) {

				line = clntIn.readUTF();
				System.out.println("Responding to client id " + myId + " " + line);
				if (line.equalsIgnoreCase("hello")) {
					clntOut.println("pollo");
					clntOut.flush();
				} else if (line.startsWith("calc")) {
					String data = line.substring(4);
					ScriptEngineManager mgr = new ScriptEngineManager();
					ScriptEngine engine = mgr.getEngineByName("JavaScript");
					try {
						String result = data + " = " + engine.eval(data);
						System.out.println("Responding to client id " + myId + " " + result);
						clntOut.println(result);
						clntOut.flush();
					} catch (ScriptException e) {
						e.printStackTrace();
					}
				} else if (line.equalsIgnoreCase("Over")) {
					isClientOpen = false;
				} else {
					clntOut.println(
							"I see you said \"" + line + "\" but I do not understand that... Please try hello or calc");
					clntOut.flush();
				}
			}
			System.out.println("Closing connection");
		} catch (IOException i) {
			System.out.println(i);
		} finally {
			try {
				clntIn.close();
				clntOut.close();
				clientSock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
