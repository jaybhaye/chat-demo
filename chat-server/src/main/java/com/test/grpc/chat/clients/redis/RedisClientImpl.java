package com.test.grpc.chat.clients.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.test.grpc.chat.clients.redis.pool.JedisConnectionPool;
import com.test.grpc.chat.server.impl.ChatServiceImpl;

import redis.clients.jedis.Jedis;

public class RedisClientImpl implements RedisClient {
	
	private static final Logger LOG = LogManager.getLogger(RedisClient.class);
	
	private Jedis getConnection() {
		return JedisConnectionPool.getInstance().getConnection();
	}
	
	private void returnConnection(Jedis jedis) {
		JedisConnectionPool.getInstance().returnConnection(jedis);
	}
	
	public void lpush(String listName, String value) {
		Jedis jedis = getConnection();
		jedis.lpush(listName, value);
		returnConnection(jedis);
	}
	
	public void ltrim(String listName, int startIdx, int endIdx) {
		Jedis jedis = getConnection();
		jedis.ltrim(listName, startIdx, endIdx);
		returnConnection(jedis);
	}
	
	public List<String> getAllList(String listName) {
		List<String> list = new ArrayList<String>();
		try {
			Jedis jedis = getConnection();
			list = jedis.lrange(listName, 0, -1);
			returnConnection(jedis);
			return list;
		} catch (Exception e) {
			LOG.error(e);
		}
		return list;
	}
	
	public void zadd(String setName, String member, Double score) {
		Jedis jedis = getConnection();
		jedis.zadd(setName, score, member);
		returnConnection(jedis);
	}
	
	public Set<String> zRangeByScore(String setName, Double start, Double end) {
		Jedis jedis = getConnection();
		Set<String> members = jedis.zrangeByScore(setName, start, end);
		returnConnection(jedis);
		return members;
	}
	
	public void zRemRangeByScore(String setName, Double start, Double end) {
		Jedis jedis = getConnection();
		jedis.zremrangeByScore(setName, start, end);
		returnConnection(jedis);
	}

	@Override
	public void del(String setName) {
		Jedis jedis = getConnection();
		jedis.del(setName);
		returnConnection(jedis);
		
	}

	@Override
	public String get(String key) {
		String value = null;
		Jedis jedis = getConnection();
		value = jedis.get(key);
		returnConnection(jedis);
		return value;
	}
}