package com.groom.yummy.reply.dto;

import com.groom.yummy.reply.Reply;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReplyUpdateRequestDto(
	@NotNull(message = "댓글 내용은 필수입니다.")
	@Size(max = 500, message = "댓글 내용은 500자를 넘을 수 없습니다.")
	String content
) {
}