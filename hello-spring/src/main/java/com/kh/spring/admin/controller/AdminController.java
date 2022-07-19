package com.kh.spring.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

	@GetMapping("/memberList.do")
	public void memberList() {
		log.debug("/admin/memberList.do 요청!");
	}
	
}
