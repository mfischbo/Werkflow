package de.artignition.werkflow.client.io;

public class MessageReceiver implements TextMessageReceiver {

	
	public void handleMessage(String text) {
		System.out.println("He said: " + text);
	}
}
