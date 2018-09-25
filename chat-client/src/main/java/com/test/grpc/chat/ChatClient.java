package com.test.grpc.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.test.grpc.chat.Chat.AuthRequest;
import com.test.grpc.chat.Chat.SendMessageRequest;
import com.test.grpc.chat.client.auth.TokenHolder;
import com.test.grpc.chat.client.factory.ResponseObserverFactory;
import com.test.grpc.chat.client.interceptors.AuthClientInterceptor;
import com.test.grpc.chat.client.types.constants.Constants;

import io.grpc.Context;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ChatClient {

	private String userId;
	private static final int PORT = 8091;
	private static final String HOST = "localhost";

	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	PrintStream out = System.out;

	ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
										.intercept(new AuthClientInterceptor()).usePlaintext(true).build();

	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.startClient();
	}

	public void startClient() {
		try {
			authenticationLoop();
			messageLoop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void messageLoop() throws Exception {
		receiveMessaegs();
		while (true) {
			out.println("Enter Recipient : Message");
			String tmp = in.readLine();
			String[] tokens = tmp.split(":");
			sendMessage(tokens[0].trim(), tokens[1].trim());
			Thread.sleep(100);
		}
	}

	private void authenticationLoop() throws Exception {
		boolean authenticated = false;
		while (!authenticated) {
			out.println("Enter Username");
			userId = in.readLine().trim();
			out.println("Enter Password");
			String password = in.readLine().trim();
			authenticate(password);
			Thread.sleep(1000); // Underlying call is Async, wait for it to return
			if (TokenHolder.getInstance().getToken() != null) {
				authenticated = true;
			}
		}
	}

	private void authenticate(String password) {

		ChatServiceGrpc.ChatServiceStub chatService = ChatServiceGrpc.newStub(channel);

		AuthRequest request = Chat.AuthRequest.newBuilder()
				                              .setUserId(userId)
				                              .setPassword(password)
				                              .build();
		Context.current()
		       .withValue(Constants.USER_ID_CONTEXT_KEY, userId)
			   .withValue(Constants.PASSWORD_CONTEXT_KEY, password).run(() -> {
					   chatService.login(request, ResponseObserverFactory.newAuthResponseObserver());});
	}

	public void receiveMessaegs() {

		ChatServiceGrpc.ChatServiceStub chatService = ChatServiceGrpc.newStub(channel);

		Context.current()
		       .withValue(Constants.USER_ID_CONTEXT_KEY, userId)
			   .withValue(Constants.AUTH_TOKEN_CONTEXT_KEY, TokenHolder.getInstance().getToken()).run(() -> {
					chatService.receiveMessage(ResponseObserverFactory.newRecceiveMessagesObserver());});
	}

	private void sendMessage(String receiver, String message) {
		ChatServiceGrpc.ChatServiceStub chatService1 = ChatServiceGrpc.newStub(channel);
		SendMessageRequest request = Chat.SendMessageRequest.newBuilder()
				                                            .setSender(userId)
				                                            .setAuthToken(TokenHolder.getInstance().getToken())
				                                            .setMessage(message)
				                                            .setReceiver(receiver)
				                                            .build();
		Context.current().withValue(Constants.USER_ID_CONTEXT_KEY, userId)
				.withValue(Constants.AUTH_TOKEN_CONTEXT_KEY, TokenHolder.getInstance().getToken()).run(() -> {
					chatService1.sendMessage(request, ResponseObserverFactory.newSendMessageResponseObserver());
				});
	}
}
