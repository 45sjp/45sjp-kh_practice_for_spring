package com.kh.spring.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.dto.Member;
import com.kh.spring.member.model.dao.MemberDao;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;
	
	@Override
	public int insertMember(Member member) {
		return memberDao.insertMember(member);
	}
	
	@Override
	public Member selectOneMember(String memberId) {
		return memberDao.selectOneMember(memberId);
	}
	
}
