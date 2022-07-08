package com.kh.spring.demo.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor // 기본생성자
@AllArgsConstructor
@ToString
public class Dev {

	private int no;
	private String name;
	private int career;
	private String email;
	private Gender gender;
	private String[] lang;
	private LocalDateTime createdAt;
	
	// Boilerplate code
	// lombok으로 처리!
}
