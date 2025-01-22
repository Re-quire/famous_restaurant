package com.groom.yummy.domain.usertogroup;

import com.groom.yummy.domain.group.GroupEntity;
import com.groom.yummy.domain.group.GroupJpaRepository;
import com.groom.yummy.domain.store.StoreEntity;
import com.groom.yummy.domain.store.StoreJpaRepository;
import com.groom.yummy.domain.user.UserEntity;
import com.groom.yummy.usertogroup.AttendanceStatus;
import com.groom.yummy.group.Group;
import com.groom.yummy.user.User;
import com.groom.yummy.usertogroup.UserToGroup;
import com.groom.yummy.usertogroup.UserToGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserToGroupEntityRepository implements UserToGroupRepository {

    private final UserToGroupJpaRepository userToGroupJpaRepository;
    private final StoreJpaRepository storeJpaRepository;

    @Override
    public void save(Group group, User user, boolean isLeader, AttendanceStatus attendanceStatus) {
        StoreEntity storeEntity = group.getStoreId() != null
                ? storeJpaRepository.findById(group.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("Store not found for id: " + group.getStoreId()))
                : null;

        GroupEntity groupEntity = GroupEntity.fromGroupDomain(group, null);
        UserEntity userEntity = UserEntity.toEntity(user);

        UserToGroupEntity userToGroupEntity = UserToGroupEntity.builder()
                .group(groupEntity)
                .user(userEntity)
                .isLeader(isLeader)
                .attendanceStatus(attendanceStatus)
                .build();

        userToGroupJpaRepository.save(userToGroupEntity);
    }

    @Override
    public Optional<UserToGroup> findByGroupAndUser(Group group, User user) {
        GroupEntity groupEntity = GroupEntity.fromGroupDomain(group, null);
        UserEntity userEntity = UserEntity.toEntity(user);

        return userToGroupJpaRepository.findById(groupEntity.getId())
                .map(entity -> UserToGroup.builder()
                        .id(entity.getId())
                        .group(group)
                        .user(user)
                        .isLeader(entity.isLeader())
                        .attendanceStatus(entity.getAttendanceStatus())
                        .build());
    }
}

