package com.kh.spring.member.model.service;

import com.kh.spring.member.dto.Member;

public interface MemberService {

	int insertMember(Member member);

	Member selectOneMember(String memberId);

	int updateMember(Member member);
	
}
