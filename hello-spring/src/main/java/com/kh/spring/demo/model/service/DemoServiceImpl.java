package com.kh.spring.demo.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.demo.model.dao.DemoDao;
import com.kh.spring.demo.model.dto.Dev;

/**
 * 기존 Service 업무
 * 	- SqlSession 생성/반환
 * 	- dao 요청
 * 	- 트랜잭션 처리
 * 
 * Spring에서의 Service 업무
 * 	- SqlSession 생성/반환을 Dao DI 받아서 처리
 * 	- 트랜잭션 처리를 AOP로 구현
 * 	- dao 요청만 처리하면 됨!
 */
@Service
public class DemoServiceImpl implements DemoService {

	@Autowired
	private DemoDao demoDao;
	
	@Override
	public int insertDev(Dev dev) {
		return demoDao.insertDev(dev);
	}
	
	@Override
	public List<Dev> selectDevList() {
		return demoDao.selectDevList();
	}

	@Override
	public Dev selectOneDev(int no) {
		return demoDao.selectOneDev(no);
	}
	
	@Override
	public int updateDev(Dev dev) {
		return demoDao.updateDev(dev);
	}
	
	@Override
	public int deleteDev(int no) {
		return demoDao.deleteDev(no);
	}

}
