package com.hahuge.myweb.redis.service;

public interface JedisService {
	public void set(String key, String value);
	public String get(String key);
	public void del(String key);
}
