package com.kh.spring.demo.model.exception;

public class DevNotFoundException extends RuntimeException {

	public DevNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DevNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DevNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DevNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DevNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
