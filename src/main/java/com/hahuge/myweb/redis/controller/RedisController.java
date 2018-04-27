package com.hahuge.myweb.redis.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hahuge.myweb.redis.service.JedisService;

@Controller
public class RedisController {
	
	@Resource(name = "jedisServiceImpl")
	private JedisService jedisService;
	@RequestMapping(value = "test")
	public void test(HttpServletRequest request){
		jedisService.set("hqpmz", request.getParameter("ss"));
		System.out.println("success");
	}
	
	@RequestMapping(value = "test1")
	public void test1(){
		System.out.println(jedisService.get("hqpmz"));
	}
}
