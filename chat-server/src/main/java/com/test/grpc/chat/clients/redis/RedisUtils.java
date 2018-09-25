package com.test.grpc.chat.clients.redis;

public class RedisUtils {
	
	private static final String MESSAGE_LIST_KEY_SUFFIX = "_messages";
	private static final String PASSWORD_KEY_SUFFIX = "_password";
	private static final String MESSAGE_SENT_TIME_LRU_KEY_SUFFIX = "_lru";
	
	public static String getMessageListKey(String userId) {
		return userId == null ? null : userId +  MESSAGE_LIST_KEY_SUFFIX;
	}
	
	public static String getPasswordKey(String userId) {
		return userId == null ? null : userId +  PASSWORD_KEY_SUFFIX;
	}
	
	public static String getMessageSentLRUKey(String userId) {
		return userId == null ? null : userId +  MESSAGE_SENT_TIME_LRU_KEY_SUFFIX;
	}
}
