package com.youandi.controller;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocketController {
	
	@OnOpen
	public void handleOpen() {
		System.out.println("Request Connecting from Client...");
	}
	
	@OnMessage
	public String handleMessage(String message) {
		System.out.printf("Received from client <= %s\n", message);
		
		String responseMessage = "Success Received.";
		
		System.out.printf("Send to client => %s\n", responseMessage);
		return responseMessage;
	}
	
	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}
	
	@OnClose
	public void handleClose() {
		System.out.println("Request Disconnecting from Client...");
	}

}
