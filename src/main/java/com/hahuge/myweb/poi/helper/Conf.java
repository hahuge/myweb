package com.hahuge.myweb.poi.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class Conf {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(Conf.class);

	public static String COOKIE_DOMAIN;

}
