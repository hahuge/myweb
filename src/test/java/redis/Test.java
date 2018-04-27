package redis;

import javax.annotation.Resource;

import com.hahuge.myweb.redis.service.JedisService;

public class Test {
	@Resource(name = "jedisServiceImpl")
	private JedisService jedisService;
	@org.junit.Test
	public void test1(){
		jedisService.set("aa", "123");
	}
}
