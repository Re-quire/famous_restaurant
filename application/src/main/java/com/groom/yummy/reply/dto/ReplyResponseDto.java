package com.groom.yummy.reply.dto;

import java.time.LocalDateTime;

import com.groom.yummy.reply.Reply;

import lombok.Builder;

@Builder
public record ReplyResponseDto(
	Long id,
	String content,
	Long parentId,
	Long userId,
	Long groupId,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	public static ReplyResponseDto fromDomain(Reply reply) {
		return ReplyResponseDto.builder()
			.id(reply.getId())
			.content(reply.getContent())
			.parentId(reply.getParentReplyId())
			.userId(reply.getUserId())
			.groupId(reply.getGroupId())
			.createdAt(reply.getCreatedAt())
			.updatedAt(reply.getUpdatedAt())
			.build();
	}
}