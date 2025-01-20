package com.groom.yummy.reply;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyRepository {
	Optional<Reply> findById(Long id);
	List<Reply> findAllByParentId(Long parentId);
	Optional<Reply> save(Reply reply);
	Page<Reply> findByGroupId(Long groupId, Pageable pageable);
}