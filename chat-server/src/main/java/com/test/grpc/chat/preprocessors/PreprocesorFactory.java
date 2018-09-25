package com.test.grpc.chat.preprocessors;

import com.test.grpc.chat.Chat;

public class PreprocesorFactory {
	public static ChainOfResponsibility<Chat.SendMessageRequest> newSendMessageProcessorChain(){
		ChainOfResponsibility<Chat.SendMessageRequest> chain = new ChainOfResponsibility<Chat.SendMessageRequest>();
		ChatMessageSizePreprocessor node = new ChatMessageSizePreprocessor();
		chain.addNode(node);
		return chain;
	}
}