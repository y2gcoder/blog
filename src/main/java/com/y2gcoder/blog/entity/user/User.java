package com.y2gcoder.blog.entity.user;

import com.y2gcoder.blog.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String email;

	private String password;

	private String nickname;

	private String profile;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserRole> roles;

	@Builder
	public User(String email, String password, String nickname, String profile, List<Role> roles) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.profile = profile;
		this.roles = roles.stream()
				.map((role) -> new UserRole(this, role))
				.collect(Collectors.toSet());
	}

	public void changeNickname(String nickname) {
		this.nickname = nickname;
	}

	public void changeProfile(String profile) {
		this.profile = profile;
	}
}
