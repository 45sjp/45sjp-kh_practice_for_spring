package com.kh.spring.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.dto.Member;
import com.kh.spring.member.model.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
@SessionAttributes({"loginMember"}) // 특정 키값에 대해 request scope가 아닌 session으로 저장하도록 지정
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
	public void memberLogin() {}
	
	@PostMapping("/memberLogin.do")
	public String memberLogin(
			@RequestParam String memberId,
			@RequestParam String password,
			RedirectAttributes redirectAttr,
			Model model) {
		
		log.info("memberId = {}, password = {}", memberId, password);
		
		try {
			Member member = memberService.selectOneMember(memberId);
			log.info("member = {}", member);
			
			if (member != null && bcryptPasswordEncoder.matches(password, member.getPassword())) {
				// redirectAttr.addFlashAttribute("msg", "로그인 성공!");
				model.addAttribute("loginMember", member);
				return "redirect:/";
			} else {
				redirectAttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
				return "redirect:/member/memberLogin.do";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
