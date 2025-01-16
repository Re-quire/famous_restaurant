package com.groom.yummy.domain.reply;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.groom.yummy.reply.Reply;
import com.groom.yummy.reply.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepository {

	private final ReplyJpaRepository jpaReplyRepository;

	@Override
	public Optional<Reply> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public List<Reply> findAllByParentId(Long parentId) {
		return List.of();
	}

	@Override
	public Reply save(Reply reply) {
		return null;
	}
}