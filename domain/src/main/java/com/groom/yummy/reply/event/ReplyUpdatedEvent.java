package com.groom.yummy.reply.event;

public record ReplyUpdatedEvent (
	Long replyId,
	String content
) {}
