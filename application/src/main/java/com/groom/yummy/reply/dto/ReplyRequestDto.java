package com.groom.yummy.reply.dto;

import com.groom.yummy.reply.Reply;

public record ReplyRequestDto(
	String content,
	Long parentId,
	Long userId,
	Long groupId
) {

	public Reply toDomain() {
		return Reply.builder()
			.content(content)
			.parentReplyId(parentId)
			.userId(userId)
			.groupId(groupId)
			.build();
	}
}