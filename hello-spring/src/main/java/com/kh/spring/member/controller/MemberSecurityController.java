package com.kh.spring.member.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberSecurityController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@GetMapping("/memberEnroll.do")
	public void memberEnroll() {}
	
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
	
	@GetMapping("/memberLogin.do")
	public void memberLogin() {}
	
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
			map.put("error", e.getMessage());

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
					.body(map);
		}
		return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
				.body(map);
	}
	
	/**
	 * SecurityContextHolder - SecurityContext - Authentication에 보관 중인 로그인한 사용자 정보 가져오기
	 * 	- Principal
	 * 	- Credentials
	 * 	- Authorities
	 * 
	 * SecurityContextHolder 사용 방법
	 * 	1. SecurityContextHolder로부터 Authentication 가져오기
	 * 	2. Authentication를 핸들러의 인자로 받기
	 * 	3. @AuthenticationPrincipal 통해서 Principal객체 받기 (#9.5. 참고!)
	 */
	// @GetMapping("/memberDetail.do")
	public void memberDetail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Member principal = (Member) authentication.getPrincipal();
		log.debug("principal = {}", principal);
		/*
			Member(super=MemberEntity(memberId=honggd, 
			password=$2a$10$wknr5gqMXSTB3gKQyEX.SO7/hREW46t3iDGMqC2xpjcf/sQhq8D7m, 
			name=홍길동동, gender=M, birthday=2000-09-09, 
			email=honggd@naver.com, phone=01012341234, address=null, 
			hobby=[운동, 등산, 독서, 게임, 여행], createdAt=2022-07-11T14:12:56, 
			updatedAt=2022-07-12T11:28:09, enabled=true), authorities=[ROLE_USER])
		 */
		
		Object credentials = authentication.getCredentials();
		log.debug("credential = {}", credentials); // null
		
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
		log.debug("authorities = {}", authorities); // [ROLE_USER]
	}
	
	// @GetMapping("/memberDetail.do")
	public void memberDetail2(Authentication authentication) {
		Member principal = (Member) authentication.getPrincipal();
		log.debug("principal = {}", principal);
		
		Object credentials = authentication.getCredentials();
		log.debug("credential = {}", credentials); // null
		
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
		log.debug("authorities = {}", authorities); // [ROLE_USER]
	}
	
	@GetMapping("/memberDetail.do")
	public void memberDetail3(@AuthenticationPrincipal Member member) {
		log.debug("member = {}", member);
	}
	
	@PostMapping("/memberUpdate.do")
	public ResponseEntity<?> memberUpate(Member updateMember, @AuthenticationPrincipal Member loginMember) {
		log.debug("updateMember = {}", updateMember);
		log.debug("loginMember = {}", loginMember);
		Map<String, Object> map = new HashMap<>();
		try {
			// 1. db 갱신
			int result = memberService.updateMember(updateMember);
			
			// 2. security가 관리하는 session 업데이트
			loginMember.setName(updateMember.getName());
			loginMember.setBirthday(updateMember.getBirthday());
			loginMember.setEmail(updateMember.getEmail());
			loginMember.setPhone(updateMember.getPhone());
			loginMember.setAddress(updateMember.getAddress());
			loginMember.setGender(updateMember.getGender());
			loginMember.setHobby(updateMember.getHobby());
			
			// 비밀번호/권한정보가 바뀌었을 때는 Authentication을 대체해주어야 함
			Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
														loginMember, loginMember.getPassword(), loginMember.getAuthorities());
			
			// 실제 Security가 관리하고 있는 Authentication을 전체 변경시킴
			SecurityContextHolder.getContext().setAuthentication(newAuthentication);
			
			map.put("msg", "회원 정보를 성공적으로 수정했습니다.");
		} catch (Exception e) {
			log.error("회원 정보 수정 오류!", e);
			map.put("msg", "회원 정보 수정 오류!");			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}		
		return ResponseEntity.ok(map);
	}
	
}
