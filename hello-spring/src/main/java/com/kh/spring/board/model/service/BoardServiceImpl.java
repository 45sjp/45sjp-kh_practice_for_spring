package com.kh.spring.board.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.dto.Attachment;
import com.kh.spring.board.model.dto.Board;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;

	@Transactional(readOnly = true) // 읽기 전용 선언
	@Override
	public List<Board> selectBoardList(int cPage, int numPerPage) {
		int offset = (cPage - 1) * numPerPage;
		int limit = numPerPage;
		RowBounds rowBounds = new RowBounds(offset, limit);
		return boardDao.selectBoardList(rowBounds);
	}

	@Override
	public int selectTotalContent() {
		return boardDao.selectTotalContent();
	}

	/**
	 * Transaction은 Runtime 예외 발생 시에만 rollback 처리
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBoard(Board board) {
		// try ~ catch을 작성하지는 않지만 AOP에서 예외발생처리를 하기 때문에 같은 기능을 함
		// insertBoard와 insertAttachment가 동시에 commit되어야 함. 하나가 실패하면 모두 rollback 처리!
		// 1. board insert
		int result = boardDao.insertBoard(board); // selectkey에서 전달한 값이 저장되기 때문에 board#no이 들어옴
		log.debug("board#no = {}", board.getNo());
		
		// 2. attachments insert
		List<Attachment> attachments = board.getAttachments();
		if(!attachments.isEmpty()) {
			for(Attachment attach : attachments) {
				attach.setBoardNo(board.getNo());
				result = boardDao.insertAttachment(attach);
			}
		}
		return result;
	}
	
	@Override
	public Board selectOneBoard(int no) {
		Board board = boardDao.selectOneBoard(no);
		List<Attachment> attachments = boardDao.findAttachmentsByBoardNo(no);
		board.setAttachments(attachments);
		return board;
	}
	
	@Override
	public Board selectOneBoardCollection(int no) {
		return boardDao.selectOneBoardCollection(no);
	}
	
	@Override
	public Attachment selectOneAttachment(int attachNo) {
		return boardDao.selectOneAttachment(attachNo);
	}
	
	@Override
	public int deleteAttachments(int attachNo) {
		return boardDao.deleteAttachments(attachNo);
	}
	
	@Override
	public int updateBoard(Board board) {
		// 1. board update
		int result = boardDao.updateBoard(board);
		log.debug("board#no = {}", board.getNo());
		
		// 2. attachments insert
		List<Attachment> attachments = board.getAttachments();
		if(!attachments.isEmpty()) {
			for(Attachment attach : attachments) {
				result = boardDao.insertAttachment(attach);
			}
		}
		return result;
	}
	
}
