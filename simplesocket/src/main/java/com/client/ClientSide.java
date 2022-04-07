package com.client;

import java.net.*;
import java.io.*;

public class ClientSide {
	DataInputStream input, in;
	DataOutputStream out;
	Socket socket;

	public ClientSide(String address, int port) {
		try {
			socket = new Socket(address, port);
			System.out.println("Connected");
			input = new DataInputStream(System.in);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			String line = " ";
			try {
				boolean isConnOpen = true;
				while (isConnOpen) {
					line = input.readLine();
					out.writeUTF(line);
					String serverSaid = in.readLine();
					if (serverSaid != null) {
						System.out.println("Server said :" + serverSaid);
					} else {
						isConnOpen = false;
					}
				}
			} catch (IOException i) {
				System.out.println(i);
			}
			input.close();
			out.close();
			socket.close();
		} catch (Exception i) {
			System.out.println(i);
		}
	}

	public static void main(String args[]) {
		ClientSide client = new ClientSide("localhost", 5230);
	}
}