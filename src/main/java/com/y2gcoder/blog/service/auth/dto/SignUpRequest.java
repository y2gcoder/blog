package com.y2gcoder.blog.service.auth.dto;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
	@Email(message = "이메일 형식을 맞춰주세요.")
	@NotBlank(message = "이메일을 입력해주세요.")
	private String email;
	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
			message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
	private String password;
	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min=2, message = "닉네임이 너무 짧습니다.")
	@Pattern(regexp = "^[A-Za-z가-힣]+$", message = "닉네임은 한글 또는 알파벳만 입력해주세요.")
	private String nickname;

	public static User toEntity(SignUpRequest req, Role role, PasswordEncoder passwordEncoder) {
		return User.builder()
				.email(req.email)
				.password(passwordEncoder.encode(req.password))
				.nickname(req.nickname)
				.roles(List.of(role))
				.build();
	}
}
