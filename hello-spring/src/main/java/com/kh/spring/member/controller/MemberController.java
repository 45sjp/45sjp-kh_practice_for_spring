package com.kh.spring.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.service.MemberService;

import lombok.extern.slf4j.Slf4j;

// @Controller
@RequestMapping("/member")
@Slf4j
@SessionAttributes({"loginMember", "next"}) // 특정 키값에 대해 request scope가 아닌 session으로 저장하도록 지정
public class MemberController {

	// @Slf4j = private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	BCryptPasswordEncoder bcryptPasswordEncoder;
	
	// @RequestMapping(path="/memberEnroll.do", method=RequestMethod.GET)
	@GetMapping("/memberEnroll.do") // 특정 전송방식으로 한정하고 싶을 때 간단하게 사용
	public String memberEnroll() {
		return "member/memberEnroll";
	}
	
	/**
	 * BCryptPasswordEncoder
	 * 
	 * $2a$10$wknr5gqMXSTB3gKQyEX.SO7/hREW46t3iDGMqC2xpjcf/sQhq8D7m
	 * 	- 알고리즘 : $2a$
	 * 	- 옵션 : 10$
	 * 	- 랜덤솔트 22자리 : wknr5gqMXSTB3gKQyEX.SO
	 * 	- 해시 31자리 : 7/hREW46t3iDGMqC2xpjcf/sQhq8D7m
	 */
	@PostMapping("/memberEnroll.do")
	public String memberEnroll(Member member, RedirectAttributes redirectAttr) {
		log.info("member = {}", member);
		try {
			// 암호화 처리
			String rawPassword = member.getPassword();
			String encryptedPassword = bcryptPasswordEncoder.encode(rawPassword);
			member.setPassword(encryptedPassword);
			log.info("encryptedPassword = {}", encryptedPassword);
			
			// service에 insert 요청
			int result = memberService.insertMember(member);
			
			// 사용자 처리 피드백
			redirectAttr.addFlashAttribute("msg", "회원가입이 성공적으로 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			throw e; // tomcat 안의 spring container에 먼저 예외가 던져짐
		}
		return "redirect:/";
	}
	
	/**
	 * VimeNameTranslator빈
	 * 	- 핸들러의 리턴타입이 void라면 요청주소를 바탕으로 viewName을 유추
	 * 	- /member/memberLogin.do -> member/memberLogin -> /WEB-INF/views/member/memberLogin.jsp
	 */
	@GetMapping("/memberLogin.do")
	public void memberLogin(
			@RequestHeader(name = "Referer", required = false) String referer,
			@SessionAttribute(required = false) String next,
			Model model) {
		log.info("referer = {}", referer);
		log.info("next = {}", next);
		
		if(next == null && referer != null)
			model.addAttribute("next", referer);
	}
	
	@PostMapping("/memberLogin.do")
	public String memberLogin(
			@RequestParam String memberId,
			@RequestParam String password,
			RedirectAttributes redirectAttr,
			@SessionAttribute(required = false) String next,
			Model model) {
		
		log.info("memberId = {}, password = {}", memberId, password);
		
		try {
			Member member = memberService.selectOneMember(memberId);
			log.info("member = {}", member);
			
			if (member != null && bcryptPasswordEncoder.matches(password, member.getPassword())) {
				// redirectAttr.addFlashAttribute("msg", "로그인 성공!");
				model.addAttribute("loginMember", member);
				
				log.info("next = {}", next);
				// model.addAttribute("next", null); // model에서 제거(작동 안 함)
				
				String location = next != null ? next : "/";
				return "redirect:" + location;
			} else {
				redirectAttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
				return "redirect:/member/memberLogin.do";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@GetMapping("/memberLogout.do")
	public String memberLogout(SessionStatus sessionStatus, ModelMap modelMap) {
		// modelMap 속성 완전 제거
		modelMap.clear(); // 완전 제거하기 위해 modelMap으로 처리!
		
		// 기존 방식 : 로그인 시 생성되었던 session 객체를 매번 폐기
		// 현재 방식 : 사용완료 마킹 처리(세션 객체 자체를 폐기하지 않음)
		if(!sessionStatus.isComplete())
			sessionStatus.setComplete(); // 제거된 속성들을 redirect할 때 url로 붙여줌
		return "redirect:/";
	}
	
	@GetMapping("/memberDetail.do")
	public void memberDetail() {}
	
	@PostMapping("/memberUpdate.do")
	public String memberUpdate(
			@ModelAttribute("loginMember") Member loginMember, // 로그인 객체를 가져와서 db에 바로 갱신!
			RedirectAttributes redirectAttr,
			Model model) {
		try {
			log.info("loginMember = {}", loginMember);
			int result = memberService.updateMember(loginMember);
			
			// 세션 정보 갱신
			// model.addAttribute("loginMember", memberService.selectOneMember(member.getMemberId()));
			
			redirectAttr.addFlashAttribute("msg", "회원정보가 성공적으로 수정되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return "redirect:/member/memberDetail.do";
	}
	//-------------------------- Spring에서 비동기 요청을 하는 방법 --------------------------
	/**
	 * 방법1 : ajax1
	 * 	- BeanNameViewResolver + jsonView
	 * 	- jsonView : model에 저장된 속성을 json으로 변환, 응답메세지에 작성
	 * 
	 * @param memberId
	 * @param model
	 * @return
	 */
	// @GetMapping("/checkIdDuplicate.do")
	public String checkIdDuplicate(@RequestParam String memberId, Model model) {
		try {
			Member member = memberService.selectOneMember(memberId);
			boolean available = member == null;
			
			model.addAttribute("memberId", memberId);
			model.addAttribute("available", available);
		} catch (Exception e) {
			log.error("중복 아이디 체크 오류!", e);
			throw e;
		}
		return "jsonView";
	}
	
	/**
	 * 방법2 : @ResponseBody + MessageConverter
	 * 	- 리턴 객체를 특정 타입(json)으로 변환해주는 빈 - jackson
	 * 	- @ResponseBody : 핸들러의 리턴 객체를 직접 응답메세지에 출력하는 방식
	 * 
	 * @param memberId
	 * @param model
	 * @return
	 */
	// @GetMapping("/checkIdDuplicate.do")
	@ResponseBody
	public Map<String, Object> checkIdDuplicate2(@RequestParam String memberId) {
		Map<String, Object> map = new HashMap<>();
		try {
			Member member = memberService.selectOneMember(memberId);
			boolean available = member == null;
			
			map.put("memberId", memberId);
			map.put("available", available);
		} catch (Exception e) {
			log.error("중복 아이디 체크 오류!", e);
			throw e;
		}
		return map;
	}
	
	/**
	 * 방법3 : ResponseEntity
	 * 	- 응답메세지 작성을 도와주는 객체. status, header, body에 자유롭게 작성 가능
	 * 	- @ResponseBody 포함
	 * 	- 기본적으로 json에 반환
	 * 
	 * @param memberId
	 * @return
	 */
	@GetMapping("/checkIdDuplicate.do")
	public ResponseEntity<?> checkIdDuplicate3(@RequestParam String memberId) {
		Map<String, Object> map = new HashMap<>();
		try {
			Member member = memberService.selectOneMember(memberId);
			boolean available = member == null;
			
			map.put("memberId", memberId);
			map.put("available", available);
		} catch (Exception e) {
			log.error("중복 아이디 체크 오류!", e);
			// throw e;
			// 사용자화해서 에러메세지 출력
			map.put("error", e.getMessage());

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR) // .status(HttpStatus.OK) == .status(200)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
					.body(map);
		}
		// return ResponseEntity.ok(map); // 200 + body에 작성할 맵
		return ResponseEntity
				.status(HttpStatus.OK) // .status(HttpStatus.OK) == .status(200)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
				.body(map);
	}
	//---------------------------------------------------------------------------------
	
}
