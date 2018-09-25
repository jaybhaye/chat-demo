package com.test.grpc.chat.types.exceptions;

public class ChainProcessingException extends Exception {
	public ChainProcessingException(String message) {
		super(message);
	}
	
	public ChainProcessingException(String message, Exception e) {
		super(message, e);
	}
}
