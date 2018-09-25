package com.test.grpc.chat.preprocessors;

import com.google.protobuf.ByteString;
import com.test.grpc.chat.Chat.SendMessageRequest;
import com.test.grpc.chat.types.exceptions.ChainProcessingException;;

public class ChatMessageSizePreprocessor 
							extends ChainOfResponsibilityNode<SendMessageRequest> {

	private static final int MAX_MESSAGE_SIZE_IN_BYTES = 4 * 1024;
	
	@Override
	protected void process(SendMessageRequest request) throws ChainProcessingException {

		if(request == null) {
			return;
		}
		
		ByteString byteString = request.getMessageBytes();
		if(byteString == null) {
			return;
		}
		
		byte[] message = byteString.toByteArray();
		if(message.length > MAX_MESSAGE_SIZE_IN_BYTES) {
			throw new ChainProcessingException("Message exceeds maximum allowed size");
		}
	}
}