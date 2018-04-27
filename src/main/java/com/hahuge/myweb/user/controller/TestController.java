package com.hahuge.myweb.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "test")
public class TestController {
	@RequestMapping(value = "index")
	public String index(){
		return "error/500";
	}
}
