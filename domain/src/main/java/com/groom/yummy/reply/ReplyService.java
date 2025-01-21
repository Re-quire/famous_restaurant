package com.groom.yummy.reply;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.yummy.group.GroupRepository;
import com.groom.yummy.publisher.EventPublisher;
import com.groom.yummy.reply.event.ReplyUpdatedEvent;
import com.groom.yummy.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyRepository replyRepository;
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final EventPublisher eventPublisher;

	@Transactional
	public Reply createReply(Reply reply) {
		validateUser(reply.getUserId());
		validateGroup(reply.getGroupId());

		Reply savedReply = replyRepository.save(reply)
			.orElseThrow(() -> new IllegalArgumentException("댓글 저장 실패: " + reply));

		return savedReply;
	}

	@Transactional
	public Reply updateReply(Long id, String content) {
		Reply reply = validateReply(id);

		// TODO: 댓글 수정 권한

		reply.updateReply(content);

		eventPublisher.publish(new ReplyUpdatedEvent(reply.getId(), reply.getContent()));

		return reply;
	}

	@Transactional(readOnly = true)
	public Page<Reply> getAllReplies(Long groupId, Pageable pageable) {
		validateGroup(groupId);
		return replyRepository.findByGroupId(groupId, pageable);
	}

	@Transactional
	public void deleteReply(Long id) {
		validateReply(id);
		replyRepository.deleteById(id);
	}

	// 댓글 검증
	private Reply validateReply(Long id) {
		return replyRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다. ID: " + id));
	}

	// 사용자 검증
	private void validateUser(Long userId) {
		userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + userId));
	}

	// 그룹 검증
	private void validateGroup(Long groupId) {
		groupRepository.findGroupById(groupId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 소모임입니다. ID: " + groupId));
	}
}
