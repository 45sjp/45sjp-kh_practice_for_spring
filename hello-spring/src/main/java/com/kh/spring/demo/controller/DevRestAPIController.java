package com.kh.spring.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.demo.model.dto.Dev;
import com.kh.spring.demo.model.exception.DevNotFoundException;
import com.kh.spring.demo.model.service.DemoService;

import lombok.extern.slf4j.Slf4j;

/**
 * RestAPI
 * --------------------------------------------------
 * 	- Representational State Transfer
 * 	- 요청 성격별로 전송방식을 결정해서 사용하는 서비스
 * 		- C : POST
 * 		- R : GET
 * 		- U : PUT(전체 데이터 교체) / PATCH(일부 데이터 교체)
 * 		- D : DELETE
 * 	- URL 작성 시에 명사형으로 계층구조를 갖도록 작성
 * 			(동사를 사용하지 않음!)
 * 		- POST /dev insertDev
 * 		- GET /dev selectDevList
 * 		- GET /dev/1 selectOneDev
 * 		- PUT /dev/1 updatedDev
 * 		- DELETE /dev/1 deleteDev
 * --------------------------------------------------
 */
@RequestMapping("/dev")
@Controller
@Slf4j
public class DevRestAPIController {

	@Autowired
	private DemoService demoService;
	
	/**
	 * ResponseEntity<T>
	 * 	- body에 작성할 자바타입
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> dev() {
		List<Dev> list = demoService.selectDevList();
		try {
			log.debug("list = {}", list);
			
		} catch (Exception e) {
			log.error("Dev 목록 조회 오류!", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "Dev 목록 조회 오류!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		return ResponseEntity.ok(list);
	}
	
	/**
	 * ResponseEntity<T>를 사용하지 않을 경우
	 * 
	 * @return
	 */
//	@GetMapping
//	@ResponseBody
//	public Object _dev() {
//		List<Dev> list = null;
//		try {
//			list = demoService.selectDevList();
//			log.debug("list = {}", list);
//		} catch (Exception e) {
//			log.error("Dev 목록 조회 오류!", e);
//			Map<String, Object> map = new HashMap<>();
//			map.put("msg", "Dev 목록 조회 오류!");
//			return map;
//		}
//		return list;
//	}
	
	@GetMapping("/{no}")
	public ResponseEntity<?> dev(@PathVariable int no) {
		Dev dev = null;
		try {
			log.debug("no = {}", no);
			dev = demoService.selectOneDev(no);
			log.debug("dev = {}", dev);
			
			if(dev == null)
				throw new DevNotFoundException(String.valueOf(no));
		} catch (DevNotFoundException e) {
			return ResponseEntity.notFound().build(); // 404
		} catch (Exception e) {
			log.error("Dev 한 명 조회 오류!", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "Dev 한 명 조회 오류!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		return ResponseEntity.ok(dev);
	}
	
	/**
	 * 406 Not Acceptable
	 * ------------------------------------------------------
	 * HTTP(HyperText Transfer Protocol) 클라이언트 오류 응답 코드는 
	 * 서버가 요청의 사전 콘텐츠 협상(406 Not Acceptable) 헤더에 정의된 
	 * 허용 가능한 값 목록과 일치하는 응답을 생성할 수 없으며 
	 * 서버가 기본 표현을 제공할 의사가 없음을 나타냅니다.
	 * ------------------------------------------------------
	 * @PathVariable에 .이 포함된 경우 정규표현식으로 작성
	 * 	=> PathVariable로 받는 변수 뒤에 :.+ 를 붙임
	 * 
	 * @param email
	 * @return
	 */
	@GetMapping(value = "/email/{email:.+}")
	public ResponseEntity<?> dev(@PathVariable String email) {
		Dev dev = null;
		try {
			log.debug("email = {}", email);
			// a. MVC 요청 새로 만들기
			// dev = demoService.selectOneDevByEmail(email);
			
			// b. 전체 목록에서 필터링하기
			List<Dev> list = demoService.selectDevList();
			for(Dev _dev : list) {
				if(email.equals(_dev.getEmail())) {
					dev = _dev;
					break;
				}
			}
			
			log.debug("dev = {}", dev);
			
			if(dev == null)
				throw new DevNotFoundException(email);
		} catch (DevNotFoundException e) {
			return ResponseEntity.notFound().build(); // 404
		} catch (Exception e) {
			log.error("Dev 한 명 이메일 조회 오류!", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "Dev 한 명 이메일 조회 오류!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
				// 406 에러는 header와 짝을 맞춰주면 해결이 됨. MediaType을 명시적으로 선언!
				.body(dev);
	}
	
	/**
	 * @실습문제 : DevRestAPI
	 * http://localhost:9090/spring/dev/lang/Java
	 * http://localhost:9090/spring/dev/lang/C
	 * http://localhost:9090/spring/dev/lang/Javascript
	 * 
	 * 해당 언어 가능한 개발자 조회하기
	 * 
	 * @param lang
	 * @return
	 */
	//----------------------------------------------------------------------------------
	// @GetMapping("/lang/{lang}")
	public ResponseEntity<?> dev2(@PathVariable String lang) {
		List<Dev> devs = new ArrayList<>();
		try {
			log.debug("lang = {}", lang);
			List<Dev> list = demoService.selectDevList();
			for(Dev dev : list) {
				String[] langs = dev.getLang();
				for(String l : langs) {
					if(l.equals(lang)) {
						devs.add(dev);
						break;
					}					
				}
			}
			log.debug("devs = {}", devs);
		} catch (DevNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("지정한 개발언어를 포함하고 있는 Devs 조회 오류!", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "지정한 개발언어를 포함하고 있는 Devs 조회 오류!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
				.body(devs);
	}
	
	@GetMapping("/lang/{lang}")
	public ResponseEntity<?> selectDevListByLang(@PathVariable String lang) {
		List<Dev> resultList = new ArrayList<>();
		log.debug("lang = {}", lang);
		lang = lang.toLowerCase();
		try {
			List<Dev> list = demoService.selectDevList();
			for(Dev dev : list) {
				String[] langs = dev.getLang();
				List<String> langList = new ArrayList<>();
				for(String _lang : langs) {
					langList.add(_lang.toLowerCase());
				}
				if(langList.contains(lang)) {
					resultList.add(dev);
				}
			}
			
			if(resultList.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			log.error("언어별 개발자 조회 오류!", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "언어별 개발자 조회 오류!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
				.body(resultList);
	}
	//----------------------------------------------------------------------------------
	
}
