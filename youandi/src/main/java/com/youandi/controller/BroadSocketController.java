package com.youandi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/broadsocket")
public class BroadSocketController {
	
	private static List<Session> sessionUsers = Collections.synchronizedList(new ArrayList<>());
	private static Pattern pattern = Pattern.compile("^\\{\\{.*?\\}\\}");
	
	@OnOpen
	public void handleOpen(Session sessionUser) {
		sessionUsers.add(sessionUser);
		
		System.out.println("Request Connecting from Client...");
	}
	
	@OnMessage
	public void handleMessage(String message, Session sessionUser) {
		System.out.printf("Recieved from client <= %s\n", message);
		
		String name = extractName(message);
		
		// Closure
		final String msg = extractMsg(message);
		final String username = makeUsername(name);
		
		sessionUsers.forEach(session -> {
			if (session == sessionUser) return;
			try {
				session.getBasicRemote().sendText(String.format("%s: %s", username, msg));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	private String extractName(String message) {
		Matcher matcher = pattern.matcher(message);
		if (matcher.find()) {
			return matcher.group();
		}
		return "{{anonymous}}";
	}
	
	private String extractMsg(String message) {
		return message.replaceAll(pattern.pattern(), "");
	}
	
	private String makeUsername(String name) {
		return name.replaceFirst("^\\{\\{", "").replaceFirst("\\}\\}$", "");
	}
	
	@OnClose
	public void handleClose(Session sessionUser) {
		sessionUsers.remove(sessionUser);
		
		System.out.println("Request Disconnecting from Client...");
	}

}
