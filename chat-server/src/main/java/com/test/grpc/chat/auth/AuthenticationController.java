package com.test.grpc.chat.auth;

import com.test.grpc.chat.clients.redis.RedisClient;
import com.test.grpc.chat.clients.redis.RedisClientImpl;
import com.test.grpc.chat.clients.redis.RedisUtils;
import com.test.grpc.chat.types.exceptions.AuthenticationFailedException;

public class AuthenticationController {
	
	private AuthTokenManager tokenManager = new AuthTokenManager();
	private RedisClient client = new RedisClientImpl();
	
	public String getToken(String userId, String password) {
		String passwordKey = RedisUtils.getPasswordKey(userId);
		String storedPassword = client.get(passwordKey);
		if(storedPassword == null || !storedPassword.equals(password)){
			return null;
		}
		return tokenManager.createAuthToken(userId);
	}
	
	public void verify(String token) throws AuthenticationFailedException {
		tokenManager.verify(token);
	}
}