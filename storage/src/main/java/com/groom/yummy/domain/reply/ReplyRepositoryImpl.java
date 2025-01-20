package com.groom.yummy.domain.reply;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.groom.yummy.domain.group.GroupEntity;
import com.groom.yummy.domain.group.GroupJpaRepository;
import com.groom.yummy.domain.user.UserEntity;
import com.groom.yummy.domain.user.UserJpaRepository;
import com.groom.yummy.reply.Reply;
import com.groom.yummy.reply.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepository {

	private final ReplyJpaRepository replyRepository;
	private final UserJpaRepository userRepository;
	private final GroupJpaRepository groupJpaRepository;

	@Override
	public Optional<Reply> findById(Long id) {
		return replyRepository.findById(id).map(ReplyEntity::toModel);
	}

	@Override
	public List<Reply> findAllByParentId(Long parentId) {
		return List.of();
	}

	@Override
	public Optional<Reply> save(Reply reply) {
		ReplyEntity parentReply = findParentReply(reply.getParentReplyId());
		UserEntity user = findUser(reply.getUserId());
		GroupEntity group = findGroup(reply.getGroupId());

		ReplyEntity replyEntity = ReplyEntity.toEntity(reply, parentReply, user, group);
		return Optional.ofNullable(ReplyEntity.toModel(replyRepository.save(replyEntity)));
	}

	@Override
	public Page<Reply> findByGroupId(Long groupId, Pageable pageable) {
		return replyRepository.findByGroupId(groupId, pageable).map(ReplyEntity::toModel);
	}

	// 부모 댓글 조회
	private ReplyEntity findParentReply(Long parentReplyId) {
		if (parentReplyId == null) {
			return null; // 부모 댓글이 없으면 null 반환
		}
		return replyRepository.findById(parentReplyId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 댓글"));
	}

	// 사용자 조회
	private UserEntity findUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
	}

	// 그룹 조회
	private GroupEntity findGroup(Long groupId) {
		return groupJpaRepository.findById(groupId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹"));
	}
}
