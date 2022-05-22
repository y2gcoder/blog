package com.y2gcoder.blog.exception;

public class UserEmailAlreadyExistsException extends RuntimeException {
	public UserEmailAlreadyExistsException(String message) {
		super(message);
	}
}
