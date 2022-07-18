package com.kh.spring.board.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kh.spring.member.dto.Member;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@NoArgsConstructor
// @AllArgsConstructor // 선언된 필드에 대해서만 처리됨
@ToString(callSuper = true)
public class Board extends BoardEntity {

	private int attachCount;
	private List<Attachment> attachments = new ArrayList<>();
	private Member member;

	public Board(int no, String title, String memberId, String content, LocalDateTime createdAt,
			LocalDateTime updatedAt, int readCount, int attachCount) {
		super(no, title, memberId, content, createdAt, updatedAt, readCount);
		this.attachCount = attachCount;
	}

	public void addAttachment(@NonNull Attachment attach) {
		attachments.add(attach);
	}
	
}
