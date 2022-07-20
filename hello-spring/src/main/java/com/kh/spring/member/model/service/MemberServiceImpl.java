package com.kh.spring.member.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.dto.Member;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDao memberDao;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertMember(Member member) {
		int result = memberDao.insertMember(member);
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", member.getMemberId());
		map.put("auth", MemberService.ROLE_USER);
		result = memberDao.insertAuthority(member); // ROLE_USER
		return result;
	}
	
	@Override
	public Member selectOneMember(String memberId) {
		return memberDao.selectOneMember(memberId);
	}
	
	@Override
	public int updateMember(Member member) {
		return memberDao.updateMember(member);
	}
	
	@Override
	public List<Member> selectMemberList() {
		return memberDao.selectMemberList();
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateMemberRole(String memberId, List<String> authorities) {
		// 기존 권한 삭제
		int result = memberDao.deleteMemeberRole(memberId);
		// 새 권한 등록
		for(String auth : authorities) {
			Map<String, Object> map = new HashMap<>();
			map.put("memberId", memberId);
			map.put("auth", auth);
			result = memberDao.insertAuthority(map);
		}
		return result;
	}
	
}
