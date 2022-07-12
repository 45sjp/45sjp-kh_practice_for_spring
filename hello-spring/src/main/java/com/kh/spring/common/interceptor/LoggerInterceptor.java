package com.kh.spring.common.interceptor;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

/**
 * Interceptor
 * 	- DispatcherServlet에서 Controller의 Handler메소드 호출 직전에 개입함
 * 	- HandlerInterceptorAdapter 상속
 * 		1. preHandle
 * 		2. postHandle
 * 		3. afterCompletion
 */
@Slf4j
public class LoggerInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("===================================================================");
		log.debug(request.getRequestURI());
		log.debug("-------------------------------------------------------------------");
		// true를 리턴해야 다음 handler로 연결될 수 있음
		return super.preHandle(request, response, handler); // 항상 true를 리턴
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		log.debug("-------------------------------------------------------------------");
		log.debug("modelAndView = {}", modelAndView);
		log.debug("-------------------------------------------------------------------");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
		log.debug("___________________________________________________________________");
		log.debug("{}\n", response.getStatus());
	}

}
