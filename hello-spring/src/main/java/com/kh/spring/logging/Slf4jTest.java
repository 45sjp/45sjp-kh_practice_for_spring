package com.kh.spring.logging;

import lombok.extern.slf4j.Slf4j;

/**
 * logging framework
 * 	- log4j
 * 	- logback
 * 	- java.util.logging
 * 	- ...
 */
@Slf4j
public class Slf4jTest {

	// private static final Logger log = LoggerFactory.getLogger(Slf4jTest.class);
	
	public static void main(String[] args) {
//		log.fatal("FATAL Message"); // slf4j에서는 지원하지 않는 레벨
		log.error("ERROR Message");
		log.warn("WARN Message");
		log.info("INFO Message");
		log.debug("DEBUG Message");
		log.trace("TRACE Message");
	}

}
