package com.test.grpc.chat.clients.redis.pool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisConnectionPool {
	
	private static final JedisConnectionPool INSTANCE = new JedisConnectionPool();
	private final JedisPool pool;
	private static final String REDIS_HOST = "localhost";
	
	private JedisConnectionPool() {
		pool = new JedisPool(new JedisPoolConfig(), REDIS_HOST);
	}
	
	public static JedisConnectionPool getInstance() {
		return INSTANCE;
	}
	
	public Jedis getConnection() {
		return pool.getResource();
	}
	
	public void returnConnection(Jedis jedis) {
		pool.returnResource(jedis);
	}
}