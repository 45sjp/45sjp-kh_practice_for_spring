package com.kh.spring.logging;

import org.apache.log4j.Logger;

/**
 * ----------------------------------------------------------------------------
 * logging framework
 * ----------------------------------------------------------------------------
 * 	- 로그의 효율적 관리가 가능
 * 	- 출력형식 : 콘솔/파일 등으로 지정
 * 	- sysout 사용을 지양할 것!
 * ---------------------------------------------------------------------------- 
 * 	- 레벨값에 따라 로깅할지 말지를 결정할 수 있음
 * 	--------------------------------------------------------------------
 * 		log4j 레벨
 * 			1. FATAL : 아주 심각한 에러
 * 			2. ERROR : 요청 중 오류 발생
 * 			3. WARN : 현재 프로그램 실행에는 문제가 없지만, 향후 문제가 될 수 있는 코드
 * 			4. INFO : 단순 정보성 메세지. 상태 변경 등
 * 			5. DEBUG : 개발 용도의 로깅에만 사용
 * 			6. TRACE : 디버그 하위에서 실행 단위를 추적 시 사용
 * 	--------------------------------------------------------------------
 * ----------------------------------------------------------------------------
 */
public class Log4jTest {
	
	private static final Logger log = Logger.getLogger(Log4jTest.class);

	public static void main(String[] args) {
		log.fatal("FATAL Message");
		log.error("ERROR Message");
		log.warn("WARN Message");
		log.info("INFO Message");
		log.debug("DEBUG Message");
		log.trace("TRACE Message");
	}

}
