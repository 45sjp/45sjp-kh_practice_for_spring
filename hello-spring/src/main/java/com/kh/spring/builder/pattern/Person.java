package com.kh.spring.builder.pattern;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Person {

	/* 필수 */
	@NonNull
	private String username;
	@NonNull
	private String password;
	
	/* 선택 */
	private LocalDate birthday;
	private String address;
	private boolean married;
	
}
