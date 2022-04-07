package com.client;

import java.io.*;
import java.net.*;

public class Client {
	public static void main(String[] args) {
		try {
			Socket s = new Socket("localhost", 8080);
			DataOutputStream output = new DataOutputStream(s.getOutputStream());
			System.out.println("hello server");
			output.writeUTF("pollo client");
			output.flush();
			output.close();
			s.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		 
	}
}
