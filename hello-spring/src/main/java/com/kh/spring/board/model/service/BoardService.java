package com.kh.spring.board.model.service;

import java.util.List;

import com.kh.spring.board.model.dto.Board;

public interface BoardService {

	List<Board> selectBoardList(int cPage, int numPerPage);

	int selectTotalContent();

	int insertBoard(Board board);

}