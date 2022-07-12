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
public class Member {

	@NonNull
	private String memberId;
	@NonNull
	private String password;
	@NonNull
	private String name;
	private Gender gender;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;
	private String email;
	@NonNull
	private String phone;
	private String address;
	private String[] hobby;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean enabled;
	
}
