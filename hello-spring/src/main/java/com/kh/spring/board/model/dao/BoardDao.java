package com.kh.spring.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import com.kh.spring.board.model.dto.Attachment;
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

	@Insert("insert into attachment (no, board_no, original_filename, renamed_filename) "
			  + "values (seq_attachment_no.nextval, #{boardNo}, #{originalFilename}, #{renamedFilename})")
	int insertAttachment(Attachment attach);

	@Select("select * from board where no = #{no}")
	Board selectOneBoard(int no);

	@Select("select * from attachment where board_no = #{no}")
	List<Attachment> findAttachmentsByBoardNo(int no);
	
	Board selectOneBoardCollection(int no);

	@Select("select * from attachment where no = #{attachNo}")
	Attachment selectOneAttachment(int attachNo);

	@Delete("delete from attachment where no = #{attachNo}")
	int deleteAttachments(int attachNo);

	@Update("update board set title = #{title}, content = #{content}, updated_at = sysdate where no = #{no}")
	int updateBoard(Board board);

}
