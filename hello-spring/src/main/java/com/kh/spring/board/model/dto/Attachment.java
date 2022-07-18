package com.kh.spring.board.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

	private int no;
	private int boardNo;
	private String originalFilename;
	private String renamedFilename;
	private int downloadCount;
	private LocalDateTime createdAt;
	
}
