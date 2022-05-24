package com.y2gcoder.blog.handler;

import com.y2gcoder.blog.controller.response.ApiFailure;
import com.y2gcoder.blog.controller.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import static com.y2gcoder.blog.exception.type.ExceptionType.BIND_EXCEPTION;
import static com.y2gcoder.blog.exception.type.ExceptionType.EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;

class FailResponseHandlerTest {
	FailResponseHandler responseHandler;

	@BeforeEach
	void beforeEach() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("i18n/exception");
		responseHandler = new FailResponseHandler(messageSource);
	}

	@Test
	void getFailureResponseNoArgsTest() {
		//given, when
		ApiResponse failureResponse = responseHandler.getFailureResponse(EXCEPTION);

		//then
		assertThat(failureResponse.getCode()).isEqualTo(-9999);
		assertThat(((ApiFailure) failureResponse.getResult()).getMsg()).isEqualTo("오류가 발생하였습니다.");
	}

	@Test
	void getFailureResponseWithArgsTest() {
		//given, when
		ApiResponse failureResponse = responseHandler.getFailureResponse(BIND_EXCEPTION, "my args");

		//then
		assertThat(failureResponse.getCode()).isEqualTo(-1003);
		assertThat(((ApiFailure) failureResponse.getResult()).getMsg()).isEqualTo("my args");
	}
}