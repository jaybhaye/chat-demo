package com.test.grpc.chat.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.grpc.chat.Chat;
import com.test.grpc.chat.clients.redis.RedisClient;
import com.test.grpc.chat.clients.redis.RedisClientImpl;
import com.test.grpc.chat.clients.redis.RedisUtils;

public class MessageStoreController {
	
	private RedisClient redis = new RedisClientImpl();
	private Gson gson = new GsonBuilder().create();
	private static final int MAX_MESSAGE_LIST_SIZE_IDX = 9;
	
	public void storeMessage(String userId, Chat.ChatMessage message) {
		String messageListName = RedisUtils.getMessageListKey(userId);
		redis.lpush(messageListName, gson.toJson(message));
		redis.ltrim(messageListName, 0, MAX_MESSAGE_LIST_SIZE_IDX);
	}
	
	public void recordMessageAsSent(String userId, Chat.ChatMessage message) {
		String messageSentLRUKeyName = RedisUtils.getMessageSentLRUKey(userId);
		long currentTime = new Date().getTime();
		redis.zadd(messageSentLRUKeyName, currentTime + "", new Double(currentTime));
	}
	
	public List<Chat.ChatMessage> getStoredMessages(String userId){
		
		String messageListName = RedisUtils.getMessageListKey(userId);
		List<Chat.ChatMessage> messages = new ArrayList<Chat.ChatMessage>();
		
		List<String> serializedMessages = redis.getAllList(messageListName);
		
		if(serializedMessages != null) {
			for(String serializedMessage : serializedMessages) {
				messages.add(gson.fromJson(serializedMessage, Chat.ChatMessage.class));
			}
		}
		return messages;
	}
	
	public int getNumberOfMessagesSentInWindow(String userId, long windowSize) {
		
		if(userId == null) {
			return 0;
		}
		
		String messageSentLRUKeyName = RedisUtils.getMessageSentLRUKey(userId);
		long currentTime = new Date().getTime();
		long windowStart = currentTime - windowSize;
		
		Set<String> members = redis.zRangeByScore(messageSentLRUKeyName, 
								new Double(windowStart), 
								new Double(currentTime));
		
		redis.zRemRangeByScore(messageSentLRUKeyName, Double.MIN_VALUE, new Double(windowStart));
		
		return members == null ? 0 : members.size();
	}
	
	public void emptyMessageList(String userId) {
		String messageListName = RedisUtils.getMessageListKey(userId);
		redis.del(messageListName);
	}
}
