package com.test.grpc.chat.client.factory;

import com.test.grpc.chat.Chat.Status;
import com.test.grpc.chat.client.auth.TokenHolder;

import io.grpc.stub.StreamObserver;
import com.test.grpc.chat.*;

public class ResponseObserverFactory {
	public static StreamObserver<Chat.AuthResponse> newAuthResponseObserver() {
		return new StreamObserver<Chat.AuthResponse>() {
			@Override
			public void onNext(Chat.AuthResponse value) {
				String token = null;
				if (value.getStatus().equals(Status.SUCCESS)) {
					token = value.getAuthToken();
				}
				TokenHolder.getInstance().setToken(token);
				System.out.println("Authentication API Result Status : " 
										+ value.getStatus() 
										+ ", authToken :  " 
										+ token);
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onCompleted() {
			}
		};
	}

	public static StreamObserver<Chat.ChatMessage> newRecceiveMessagesObserver() {
		return new StreamObserver<Chat.ChatMessage>() {
			@Override
			public void onNext(Chat.ChatMessage value) {
				System.out.println("Received Message. Sender : " + value.getSender() 
									+ ", Message : " + value.getMessage());
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onCompleted() {
			}
		};
	}

	public static StreamObserver<Chat.SendMessageResponse> newSendMessageResponseObserver() {
		return new StreamObserver<Chat.SendMessageResponse>() {
			@Override
			public void onNext(Chat.SendMessageResponse value) {
				System.out.println("Send Message API Response From Server : " + value);
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onCompleted() {}
		};
	}
}
