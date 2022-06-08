package com.y2gcoder.blog.exception.type;

import lombok.Getter;

@Getter
public enum ExceptionType {
	EXCEPTION("exception.code", "exception.msg"),
	AUTHENTICATION_ENTRY_POINT_EXCEPTION("authenticationEntryPointException.code", "authenticationEntryPointException.msg"),
	ACCESS_DENIED_EXCEPTION("accessDeniedException.code", "accessDeniedException.msg"),
	BIND_EXCEPTION("bindException.code", "bindException.msg"),
	LOGIN_FAILURE_EXCEPTION("loginFailureException.code", "loginFailureException.msg"),
	USER_EMAIL_ALREADY_EXISTS_EXCEPTION("userEmailAlreadyExistsException.code", "userEmailAlreadyExistsException.msg"),
	USER_NOT_FOUND_EXCEPTION("userNotFoundException.code", "userNotFoundException.msg"),
	ROLE_NOT_FOUND_EXCEPTION("roleNotFoundException.code", "roleNotFoundException.msg"),
	MISSING_REQUEST_HEADER_EXCEPTION("missingRequestHeaderException.code", "missingRequestHeaderException.msg"),
	REFRESH_TOKEN_FAILURE_EXCEPTION("refreshTokenFailureException.code", "refreshTokenFailureException.msg"),
	CATEGORY_NOT_FOUND_EXCEPTION("categoryNotFoundException.code", "categoryNotFoundException.msg"),

	POST_NOT_FOUND_EXCEPTION("postNotFoundException.code","postNotFoundException.msg");

	private final String code;
	private final String message;

	ExceptionType(String code, String message) {
		this.code = code;
		this.message = message;
	}

}
