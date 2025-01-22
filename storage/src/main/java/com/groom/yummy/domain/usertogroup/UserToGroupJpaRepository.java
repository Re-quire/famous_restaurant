package com.groom.yummy.domain.usertogroup;

import com.groom.yummy.domain.group.GroupEntity;
import com.groom.yummy.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserToGroupJpaRepository extends JpaRepository<UserToGroupEntity, Long> {
    Optional<UserToGroupEntity> findByGroupAndUser(GroupEntity group, UserEntity user);
}
