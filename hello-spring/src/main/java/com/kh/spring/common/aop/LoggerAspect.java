package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

/**
 * #11.1. Aspect 작성 (Pointcut + Advice)
 */
@Component
@Aspect // 클래스 레벨(한 개 이상의 Pointcut과 Advice의 조합)
@Slf4j
public class LoggerAspect {
	
	/**
	 * com.kh.spring.todo패키지 하위의 모든 클래스 모든 메소드
	 * 	- 매개변수 상관 없음
	 * 	- 리턴타입 상관 없음
	 */
	@Pointcut("execution(* com.kh.spring.todo..*(..))")
	// @Pointcut("execution(* com.kh.spring.todo.controller.TodoController.todoList(String))")
	public void pointcut() {}

	/**
	 * Joinpont 앞뒤에서 실행되는 Around Advice
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("pointcut()")
	public Object logger(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature signature = joinPoint.getSignature();
		String type = signature.getDeclaringTypeName(); // 클래스명
		String method = signature.getName(); // 메소드명
		
		// before
		log.debug("[Before] {}.{}", type, method);
		
		Object returnObj = joinPoint.proceed();
		
		// after
		log.debug("[After] {}.{}", type, method);
		
		return returnObj;
	}
	
	/**
	 * @실습문제
	 * 	Around Advice - insertTodo메소드의 실행시간을 구하시오.
	 * 	스프링이 제공하는 org.springframework.util.StopWatch를 사용할 것.
	 */
	@Pointcut("execution(* com.kh.spring.todo.controller.TodoController.insertTodo(..))")
	public void stopWatchPointcut() {}
	
	@Around("stopWatchPointcut()")
	public Object stopWatch(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object returnObj = joinPoint.proceed();
		stopWatch.stop();
		log.debug("insertTodo메소드의 실행시간 = {}초", stopWatch.getTotalTimeSeconds());
		return returnObj;
	}
	
}
