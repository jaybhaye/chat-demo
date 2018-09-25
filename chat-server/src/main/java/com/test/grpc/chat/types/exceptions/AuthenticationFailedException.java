package com.test.grpc.chat.types.exceptions;

public class AuthenticationFailedException extends Exception {
	public AuthenticationFailedException(String message) {
		super(message);
	}
	
	public AuthenticationFailedException(String message, Exception e) {
		super(message, e);
	}
}
