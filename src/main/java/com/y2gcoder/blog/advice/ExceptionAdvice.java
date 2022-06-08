package com.y2gcoder.blog.advice;

import com.y2gcoder.blog.controller.response.ApiResponse;
import com.y2gcoder.blog.exception.*;
import com.y2gcoder.blog.exception.type.ExceptionType;
import com.y2gcoder.blog.handler.FailResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.y2gcoder.blog.exception.type.ExceptionType.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
	private final FailResponseHandler responseHandler;

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse exception(Exception e) {
		log.error("Unknown Exception!! ", e);
		return getFailureResponse(EXCEPTION);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiResponse accessDeniedException() {
		return getFailureResponse(ACCESS_DENIED_EXCEPTION);
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse bindException(BindException e) {
		return getFailureResponse(
				BIND_EXCEPTION,
				e.getBindingResult().getFieldError().getDefaultMessage()
		);
	}

	@ExceptionHandler(LoginFailureException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse loginFailureException() {
		return getFailureResponse(LOGIN_FAILURE_EXCEPTION);
	}

	@ExceptionHandler(UserEmailAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiResponse userEmailAlreadyExistsException(UserEmailAlreadyExistsException e) {
		return getFailureResponse(USER_EMAIL_ALREADY_EXISTS_EXCEPTION, e.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse userNotFoundException() {
		return getFailureResponse(USER_NOT_FOUND_EXCEPTION);
	}

	@ExceptionHandler(RoleNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse roleNotFoundException() {
		return getFailureResponse(ROLE_NOT_FOUND_EXCEPTION);
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse missingRequestHeaderException(MissingRequestHeaderException e) {
		return getFailureResponse(MISSING_REQUEST_HEADER_EXCEPTION, e.getHeaderName());
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse categoryNotFoundException() {
		return getFailureResponse(CATEGORY_NOT_FOUND_EXCEPTION);
	}

	@ExceptionHandler(PostNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse postNotFoundException() {
		return getFailureResponse(POST_NOT_FOUND_EXCEPTION);
	}

	@ExceptionHandler(RefreshTokenFailureException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse refreshTokenFailureException() {
		return getFailureResponse(REFRESH_TOKEN_FAILURE_EXCEPTION);
	}

	private ApiResponse getFailureResponse(ExceptionType exceptionType) {
		return responseHandler.getFailureResponse(exceptionType);
	}

	private ApiResponse getFailureResponse(ExceptionType exceptionType, Object... args) {
		return responseHandler.getFailureResponse(exceptionType, args);
	}
}
