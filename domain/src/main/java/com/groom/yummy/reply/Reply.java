package com.groom.yummy.reply;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Reply {
	private Long id;
	private String content;
	private Long parentReplyId;
	private Long userId;
	private Long groupId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

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

	protected void updateReply(String content) {
		this.content = content;
		this.updatedAt = LocalDateTime.now();
	}

}