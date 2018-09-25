package com.test.grpc.chat.message;

import com.test.grpc.chat.Chat;
import com.test.grpc.chat.repositories.ClientRepository;
import com.test.grpc.chat.repositories.ClientRepositoryImpl;
import com.test.grpc.chat.types.Client;
import com.test.grpc.chat.types.exceptions.MessageRateExceededException;

public class MessageRouter {
	
	private static MessageRouter INSTANCE = new MessageRouter();
	private ClientRepository clientRepository = ClientRepositoryImpl.getInstance();
	private MessageRouter() {}
	
	public static MessageRouter getInstance() {
		return INSTANCE;
	}
	
	private MessageStoreController messageStore = new MessageStoreController();
	private static final int MAX_NUMBER_OF_MESSAGES_IN_A_WINDOW = 3;
	private static final int WINDOW_SIZE_IN_MILLISECONDS = 5 * 1000;
	
	public void onClientConnect(Client client) throws MessageRateExceededException  {
		clientRepository.addClient(client);
		sendStoredMessages(client.getClientId());
	}
	
	public void sendOrStoreMessage(String clientId, Chat.ChatMessage message) throws MessageRateExceededException  {
		if(!sendMessage(message.getSender(), clientId, message, true)) {
			messageStore.storeMessage(clientId, message);
			messageStore.recordMessageAsSent(message.getSender(), message);
		}
	}
	
	private boolean sendMessage(String sender, String receiver, Chat.ChatMessage message, boolean checkRate) 
													throws MessageRateExceededException {
		Client client = clientRepository.getClient(receiver);
		if(checkRate && (messageStore.getNumberOfMessagesSentInWindow(sender, WINDOW_SIZE_IN_MILLISECONDS)
									>= MAX_NUMBER_OF_MESSAGES_IN_A_WINDOW)) {
			throw new MessageRateExceededException("Message rate exceeded");
		}
		if(client != null && client.sendMessage(message)) {
			messageStore.recordMessageAsSent(sender, message);
			return true;
		}
		return false;
	}
	
	private void sendStoredMessages(String clientId) throws MessageRateExceededException {
		for(Chat.ChatMessage message : messageStore.getStoredMessages(clientId)) {
			sendMessage(message.getSender(), clientId, message, false);
		}
		messageStore.emptyMessageList(clientId);
	}
}
