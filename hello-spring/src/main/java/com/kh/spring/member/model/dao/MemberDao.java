package com.kh.spring.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.spring.member.dto.Member;

/**
 * #8. Dao 구현클래스 없이 mapper 연결하기
 */
@Mapper
public interface MemberDao {

	int insertMember(Member member);

	Member selectOneMember(String memberId);

	int updateMember(Member member);

}
