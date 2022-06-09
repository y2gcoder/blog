package com.y2gcoder.blog.config.security.guard;

import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class PostGuard extends Guard {
	private final PostRepository postRepository;
	private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

	@Override
	protected List<RoleType> getRoleTypes() {
		return roleTypes;
	}

	@Override
	protected boolean isResourceOwner(Long id) {
		return postRepository.findById(id)
				.map(Post::getUser)
				.map(User::getId)
				.filter(userId -> userId.equals(AuthHelper.extractUserId()))
				.isPresent();
	}
}
