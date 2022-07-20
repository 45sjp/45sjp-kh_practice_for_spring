package com.kh.spring.member.model.service;

import java.util.List;

import com.kh.spring.member.model.dto.Member;

public interface MemberService {

	String ROLE_USER = "ROLE_USER";
	String ROLE_ADMIN = "ROLE_ADMIN";

	int insertMember(Member member);

	Member selectOneMember(String memberId);

	int updateMember(Member member);

	List<Member> selectMemberList();

	int updateMemberRole(String memberId, List<String> authorities);
	
}
