package com.test.grpc.chat.client.auth;

public class TokenHolder {
	private static TokenHolder INSTANCE = new TokenHolder();
	private TokenHolder() {}
	public static TokenHolder getInstance() {
		return INSTANCE;
	}
	
	private String token;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
