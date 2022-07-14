package com.kh.spring.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.kh.spring.board.model.dto.Board;

@Mapper // bean으로 등록된 것이 아님. 동적으로 자식클래스가 생성되고 그것이 등록되는 것임!
public interface BoardDao {

	List<Board> selectBoardList(RowBounds rowBounds);
	
	@Select("select count(*) from board")
	int selectTotalContent();

	/*
	 * @Insert("insert into board (no, title, member_id, content) " +
	 * "values (seq_board_no.nextval, #{title}, #{memberId}, #{content})")
	 */
	int insertBoard(Board board);

}
