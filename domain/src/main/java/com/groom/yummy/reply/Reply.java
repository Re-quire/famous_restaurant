package com.groom.yummy.reply;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Reply {
	private final Long id;
	private final String content;
	private final Long parentReplyId;
	private final Long userId;
	private final Long groupId;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	@Builder
	public Reply(Long id, String content, Long parentReplyId, Long userId, Long groupId, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.content = content;
		this.parentReplyId = parentReplyId;
		this.userId = userId;
		this.groupId = groupId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}