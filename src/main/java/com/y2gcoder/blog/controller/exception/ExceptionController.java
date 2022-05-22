package com.y2gcoder.blog.controller.exception;

import com.y2gcoder.blog.exception.AuthenticationEntryPointException;
import com.y2gcoder.blog.exception.CustomAccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/exception")
@RestController
public class ExceptionController {
	@GetMapping("/entry-point")
	public void entryPoint() {
		throw new AuthenticationEntryPointException();
	}

	@GetMapping("/access-denied")
	public void accessDenied() {
		throw new CustomAccessDeniedException();
	}
}
