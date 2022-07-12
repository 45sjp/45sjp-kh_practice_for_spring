package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.kh.spring.member.dto.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Member loginMember = (Member) session.getAttribute("loginMember");
		
		if(loginMember == null) {
			log.debug("비로그인 상태에서 접근!!!");
			
			// RedirectAttributes 사용 불가
			// 대신 FlashMap을 이용해서 사용자 메세지 전달
			FlashMap flashMap = new FlashMap();
			flashMap.put("msg", "로그인 후 이용할 수 있습니다.");
			FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
			flashMapManager.saveOutputFlashMap(flashMap, request, response);
			
			// 로그인 성공 시에 redirect할 수 있도록 현재 요청 주소를 next에 담기
			// http://localhost:9090/spring/board/boardDetail.do?no=123&type=memo
			String url = request.getRequestURL().toString();
			String queryString = request.getQueryString(); // no=123&type=memo
			if(queryString != null)
				url += "?" + queryString;
			
			session.setAttribute("next", url);
			
			response.sendRedirect(request.getContextPath() + "/member/memberLogin.do");
			
			return false;
		}
		
		return super.preHandle(request, response, handler);
	}
}
