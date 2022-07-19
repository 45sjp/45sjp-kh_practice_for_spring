package com.kh.spring.member.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.kh.spring.demo.model.dto.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

// @NoArgsConstructor
// @AllArgsConstructor
// @Getter
// @Setter
// @ToString

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor // @NonNull만 있는 생성자
public class MemberEntity {

	@NonNull
	protected String memberId;
	@NonNull
	protected String password;
	@NonNull
	protected String name;
	protected Gender gender;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected LocalDate birthday;
	protected String email;
	@NonNull
	protected String phone;
	protected String address;
	protected String[] hobby;
	protected LocalDateTime createdAt;
	protected LocalDateTime updatedAt;
	protected boolean enabled;
	
}
