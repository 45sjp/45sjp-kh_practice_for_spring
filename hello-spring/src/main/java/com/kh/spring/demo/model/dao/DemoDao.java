package com.kh.spring.demo.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.kh.spring.demo.model.dto.Dev;

public interface DemoDao {

	int insertDev(Dev dev);

	List<Dev> selectDevList();

	Dev selectOneDev(int no);

	int updateDev(Dev dev);

	int deleteDev(int no);

	@Select("select * from dev where email = #{email}")
	Dev selectOneDevByEmail(String email);

}
