package com.kh.spring.builder.pattern;

import java.time.LocalDate;

/**
 * Builder 패턴
 * 	- GoF 디자인 패턴의 생성 패턴 중 하나
 * 	- 객체의 필드를 단계적으로 설정하는 방식
 */
public class User {

	/* 필수 */
	private String username;
	private String password;
	
	/* 선택 */
	private LocalDate birthday;
	private String address;
	private boolean married;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public User(String username, String password, LocalDate birthday, String address, boolean married) {
		super();
		this.username = username;
		this.password = password;
		this.birthday = birthday;
		this.address = address;
		this.married = married;
	}
	
	public User(Builder builder) {
		this.username = builder.username;
		this.password = builder.password;
		this.birthday = builder.birthday;
		this.address = builder.address;
		this.married = builder.married;
	}

	// builder 내부 클래스
	public static class Builder {
		
		/* 필수 */
		private String username;
		private String password;
		
		/* 선택 */
		private LocalDate birthday;
		private String address;
		private boolean married;
		
		public Builder(String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		public Builder birthday(LocalDate birthday) {
			this.birthday = birthday;
			return this;
		}
		
		public Builder address(String address) {
			this.address = address;
			return this;
		}
		
		public Builder married(boolean married) {
			this.married = married;
			return this;
		}
		
		// Builder 객체를 받는 생성자
		public User build() {
			return new User(this);
		}
		
	}
	
	public static Builder builder(String username, String password) {
		return new Builder(username, password);
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", birthday=" + birthday + ", address="
				+ address + ", married=" + married + "]";
	}
		
}
