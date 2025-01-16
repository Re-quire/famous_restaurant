package com.groom.yummy.reply;


import org.springframework.stereotype.Service;

import com.groom.yummy.user.User;
import com.groom.yummy.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyRepository replyRepository;
	private final UserRepository userRepository;

	public Reply createReply(Reply reply) {
		User user = userRepository.findById(reply.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		Reply parentReply = reply.getParentReplyId() != null
			? replyRepository.findById(reply.getParentReplyId())
			.orElseThrow(() -> new IllegalArgumentException("Parent reply not found"))
			: null;

		return replyRepository.save(reply);
	}
}