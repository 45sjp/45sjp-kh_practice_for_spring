package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class StopWatchAspect {

	/**
	 * @실습문제
	 * 	Around Advice - insertTodo메소드의 실행시간을 구하시오.
	 * 	스프링이 제공하는 org.springframework.util.StopWatch를 사용할 것.
	 */
	@Pointcut("execution(* com.kh.spring.todo.controller.TodoController.insertTodo(..))")
	public void pointcut() {}
	
	@Around("pointcut()")
	public Object stopWatch(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object returnObj = joinPoint.proceed();
		stopWatch.stop();
		
		long duration = stopWatch.getTotalTimeMillis();
		double duration2 = stopWatch.getTotalTimeSeconds();
		log.debug("insertTodo메소드의 실행시간 = {}ms", duration); // 25ms
		log.debug("insertTodo메소드의 실행시간 = {}s", duration2); // 0.025s
		
		return returnObj;
	}
	
}
