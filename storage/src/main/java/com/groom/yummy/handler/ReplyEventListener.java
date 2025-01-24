package com.groom.yummy.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.groom.yummy.domain.reply.ReplyEntity;
import com.groom.yummy.domain.reply.ReplyJpaRepository;
import com.groom.yummy.reply.event.ReplyUpdatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReplyEventListener {

	private final ReplyJpaRepository replyJpaRepository;

	@EventListener
	public void handleReplyUpdatedEvent(ReplyUpdatedEvent event) {
		ReplyEntity replyEntity = replyJpaRepository.findById(event.replyId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글의 아이디"));

		replyEntity.updateReply(event.content());
		log.info("댓글이 수정되었습니다. ID: {}, 내용: {}", event.replyId(), event.content());
	}
}