package com.y2gcoder.blog.advice;

import com.y2gcoder.blog.controller.response.ApiResponse;
import com.y2gcoder.blog.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse exception(Exception e) {
		log.error("Unknown Exception!! ", e);
		return ApiResponse.failure(-9999, "서버 오류가 발생했습니다.");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
		return ApiResponse.failure(-1001, e.getBindingResult().getFieldError().getDefaultMessage());
	}

	@ExceptionHandler(LoginFailureException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse loginFailureException() {
		return ApiResponse.failure(-1002, "로그인에 실패했습니다.");
	}

	@ExceptionHandler(UserEmailAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiResponse userEmailAlreadyExistsException(UserEmailAlreadyExistsException e) {
		return ApiResponse.failure(-1003, "중복된 이메일입니다. 이메일=" + e.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse userNotFoundException() {
		return ApiResponse.failure(-1004, "요청한 회원을 찾을 수 없습니다.");
	}

	@ExceptionHandler(RoleNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse roleNotFoundException() {
		return ApiResponse.failure(-1005, "요청한 권한 등급을 찾을 수 없습니다.");
	}

	@ExceptionHandler(AuthenticationEntryPointException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse authenticationEntryPointException() {
		return ApiResponse.failure(-1006, "인증되지 않은 사용자입니다.");
	}

	@ExceptionHandler(CustomAccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiResponse customAccessDeniedException() {
		return ApiResponse.failure(-1007, "접근이 거부되었습니다.");
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse missingRequestHeaderException(MissingRequestHeaderException e) {
		return ApiResponse.failure(-1008, e.getHeaderName() + " 요청 헤더가 누락되었습니다.");
	}
}
