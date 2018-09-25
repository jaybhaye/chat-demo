package com.test.grpc.chat.types;

import com.test.grpc.chat.Chat;

import io.grpc.stub.StreamObserver;

public class Client {
	
	String clientId;
	StreamObserver<Chat.ChatMessage> connection;
	
	public Client withClientId(String userId) {
		this.clientId = userId;
		return this;
	}

	public String getClientId() {
		return this.clientId;
	}
	
	public Client withConnection(StreamObserver<Chat.ChatMessage> connection) {
		this.connection = connection;
		return this;
	}
	
	public boolean sendMessage(Chat.ChatMessage message) {
		try {
			this.connection.onNext(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
