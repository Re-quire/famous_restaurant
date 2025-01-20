package com.groom.yummy.reply.dto;

import com.groom.yummy.reply.Reply;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReplyRequestDto(
	@NotNull(message = "댓글 내용은 필수입니다.")
	@Size(max = 500, message = "댓글 내용은 500자를 넘을 수 없습니다.")
	String content,

	Long parentId,

	@NotNull(message = "작성자 ID는 필수입니다.")
	Long userId,

	@NotNull(message = "그룹 ID는 필수입니다.")
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