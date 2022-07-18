package com.kh.spring.demo.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.demo.model.dto.Dev;

@Repository
public class DemoDaoImpl implements DemoDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public int insertDev(Dev dev) {
		return sqlSession.insert("demo.insertDev", dev);
	}

	@Override
	public List<Dev> selectDevList() {
		return sqlSession.selectList("demo.selectDevList");
	}

	@Override
	public Dev selectOneDev(int no) {
		return sqlSession.selectOne("demo.selectOneDev", no);
	}
	
	@Override
	public int updateDev(Dev dev) {
		return sqlSession.update("demo.updateDev", dev);
	}
	
	@Override
	public int deleteDev(int no) {
		return sqlSession.delete("demo.deleteDev", no);
	}
	
	@Override
	public Dev selectOneDevByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}
}
