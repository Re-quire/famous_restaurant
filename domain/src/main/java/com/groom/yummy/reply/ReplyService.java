package com.groom.yummy.reply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.yummy.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyRepository replyRepository;

	@Transactional
	public Reply createReply(Reply reply) {
		return replyRepository.save(reply).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
	}

	@Transactional
	public Reply updateReply(Long id, String content) {
		Reply existingReply = replyRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글이 존재하지 않습니다. ID: " + id));

		// TODO: 댓글 수정 권한

		existingReply.updateReply(content);

		return replyRepository.save(existingReply).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
	}

	@Transactional(readOnly = true)
	public Page<Reply> getAllReplies(Long groupId, Pageable pageable) {
		return replyRepository.findByGroupId(groupId, pageable);
	}
}
