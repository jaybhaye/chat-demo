package com.test.grpc.chat.clients.redis;

import java.util.List;
import java.util.Set;

public interface RedisClient {
	public String get(String key);
	public void lpush(String listName, String value);
	public void ltrim(String listName, int startIdx, int endIdx);
	public List<String> getAllList(String listName);
	public void zadd(String setName, String member, Double score);
	public Set<String> zRangeByScore(String setName, Double start, Double end);
	public void zRemRangeByScore(String setName, Double start, Double end);
	public void del(String setName);
}
