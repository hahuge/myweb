package com.hahuge.myweb.redis.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hahuge.myweb.redis.service.JedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisServiceImpl implements JedisService{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    private JedisPool jedisPool;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

	@Override
	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			 logger.error("Jedis Error:" + e.getMessage(), e);
		} finally {
			if (jedis != null) {
                jedis.close();
            }
		}
	}

	@Override
	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String value = jedis.get(key);
			return value;
		} catch (Exception e) {
			 logger.error("Jedis Error:" + e.getMessage(), e);
		} finally {
			if (jedis != null) {
                jedis.close();
            }
		}
		return null;
	}

	@Override
	public void del(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			 logger.error("Jedis Error:" + e.getMessage(), e);
		} finally {
			if (jedis != null) {
                jedis.close();
            }
		}
	}
}
