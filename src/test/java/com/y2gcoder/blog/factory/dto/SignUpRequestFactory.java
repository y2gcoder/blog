package com.y2gcoder.blog.factory.dto;

import com.y2gcoder.blog.service.auth.dto.SignUpRequest;

public class SignUpRequestFactory {
	public static SignUpRequest createSignUpRequest() {
		return new SignUpRequest("email@email.com", "1q2w3e4r!", "nickname");
	}

	public static SignUpRequest createSignUpRequest(String email, String password, String nickname) {
		return new SignUpRequest(email, password, nickname);
	}

	public static SignUpRequest createSignUpRequestWithEmail(String email) {
		return new SignUpRequest(email, "1q2w3e4r!", "nickname");
	}

	public static SignUpRequest createSignUpRequestWithPassword(String password) {
		return new SignUpRequest("email@email.com", password, "nickname");
	}

	public static SignUpRequest createSignUpRequestWithNickname(String nickname) {
		return new SignUpRequest("email@email.com", "1q2w3e4r!", nickname);
	}
}
