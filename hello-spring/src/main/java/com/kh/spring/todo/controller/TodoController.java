package com.kh.spring.todo.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.todo.model.dto.Todo;
import com.kh.spring.todo.model.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/todo")
@Slf4j
public class TodoController {

	/**
	 * Proxy객체
	 * 	- 대리자 객체
	 * 	- 작성한 구현클래스가 아닌 스프링이 제공하는 proxy객체를 의존주입 받았기 때문에
	 * 	  호출한 서비스 메소드 이외의 부가기능을 실행할 수 있음
	 * 
	 * 	- 인터페이스 구현객체 - jdk 동적 proxy객체 주입
	 * 	- 일반클래스 클래스객체 - cglib 의존이 생성한 proxy객체 주입
	 */
	@Autowired
	private TodoService todoService;
	
	@GetMapping("/todoList.do")
	public ModelAndView todoList(ModelAndView mav) {
		log.debug("todoService = {}, type = {}", todoService, todoService.getClass());
		// todoService = com.kh.spring.todo.model.service.TodoServiceImpl@23b084c8, type = class com.sun.proxy.$Proxy41
		try {
			List<Todo> todoList = todoService.selectTodoList();
			log.debug("todoList = {}", todoList);
			mav.addObject("todoList", todoList);
		} catch (Exception e) {
			log.error("할일 목록 조회 오류", e);
		}		
		return mav;
	}
	
	@PostMapping("/insertTodo.do")
	public String insertTodo(Todo todo, RedirectAttributes redirectAttr) {
		try {
			// 업무 로직
			int result = todoService.insertTodo(todo);
			redirectAttr.addFlashAttribute("msg", "할일을 성공적으로 저장했습니다.");
		} catch (Exception e) {
			log.error("할일 등록 오류", e);
			throw e;
		}
		return "redirect:/todo/todoList.do";
	}
	
	@PostMapping("/updateTodo.do")
	public String updateTodo(@RequestParam int no, @RequestParam boolean isCompleted) {
		try {
			// 업무 로직
			Map<String, Object> param = new HashMap<>();
			param.put("no", no);
			param.put("isCompleted", isCompleted);
			int result = todoService.updateTodo(param);
		} catch (Exception e) {
			log.error("할일 등록 오류", e);
			throw e;
		}
		return "redirect:/todo/todoList.do";
	}
}
