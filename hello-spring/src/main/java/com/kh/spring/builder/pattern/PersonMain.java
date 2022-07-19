package com.kh.spring.builder.pattern;

import java.time.LocalDate;

public class PersonMain {

	public static void main(String[] args) {
		
		Person person = Person.builder()
							.username("honggd") // @NonNull
							.password("1234") // @NonNull
							.birthday(LocalDate.now())
							.build();
		
		System.out.println(person);
	}

}
