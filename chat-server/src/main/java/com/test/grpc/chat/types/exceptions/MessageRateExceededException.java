package com.test.grpc.chat.types.exceptions;

public class MessageRateExceededException extends Exception {
	public MessageRateExceededException(String message) {
		super(message);
	}
	
	public MessageRateExceededException(String message, Exception e) {
		super(message, e);
	}
}
