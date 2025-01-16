package com.groom.yummy.reply;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository {
	Optional<Reply> findById(Long id);
	List<Reply> findAllByParentId(Long parentId);
	Reply save(Reply reply);
}