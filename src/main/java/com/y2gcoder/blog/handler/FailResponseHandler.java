package com.y2gcoder.blog.handler;

import com.y2gcoder.blog.controller.response.ApiResponse;
import com.y2gcoder.blog.exception.type.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FailResponseHandler {
	private final MessageSource messageSource;

	public ApiResponse getFailureResponse(ExceptionType exceptionType) {
		return ApiResponse.failure(getCode(exceptionType.getCode()), getMessage(exceptionType.getMessage()));
	}

	public ApiResponse getFailureResponse(ExceptionType exceptionType, Object... args) {
		return ApiResponse.failure(getCode(exceptionType.getCode()), getMessage(exceptionType.getMessage(), args));
	}

	private Integer getCode(String key) {
		return Integer.valueOf(messageSource.getMessage(key, null, null));
	}

	private String getMessage(String key) {
		return messageSource.getMessage(key,null, LocaleContextHolder.getLocale());
	}

	private String getMessage(String key, Object... args) {
		return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
	}
}
