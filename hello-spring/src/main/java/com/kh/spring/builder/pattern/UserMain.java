package com.kh.spring.builder.pattern;

import java.time.LocalDate;

public class UserMain {

	public static void main(String[] args) {
		
		User user = new User("sejong", "1234", null, null, false);

		User user1 = User.builder("honggd", "1234").build();
		User user2 = User.builder("sinsa", "1234")
				// 필수값이 아니라면 순서 상관 없음. 사용하지 않으면 삭제 또한 가능!
				.birthday(LocalDate.of(1999, 9, 9)) // Month는 작성한 그대로임!
				.address("서울시 강서구")
				.married(true)
				.build();
		
		System.out.println(user1);
		System.out.println(user2);
	}

}
