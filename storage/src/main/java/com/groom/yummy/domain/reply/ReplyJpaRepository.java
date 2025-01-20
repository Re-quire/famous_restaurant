package com.groom.yummy.domain.reply;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyJpaRepository extends JpaRepository<ReplyEntity, Long> {
	Page<ReplyEntity> findByGroupId(Long groupId, Pageable pageable);

	Page<ReplyEntity> findByParentReplyId(Long parentId, Pageable pageable);

	boolean existsByParentReplyId(Long id);
}
