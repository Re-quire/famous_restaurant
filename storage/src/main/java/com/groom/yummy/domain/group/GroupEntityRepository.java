package com.groom.yummy.domain.group;

import com.groom.yummy.domain.store.StoreEntity;
import com.groom.yummy.domain.store.StoreJpaRepository;
import com.groom.yummy.group.Group;
import com.groom.yummy.group.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class GroupEntityRepository implements GroupRepository {

    private final GroupJpaRepository groupJpaRepository;

    @Override
    public Long saveGroup(Group group) {
//        StoreEntity storeEntity = storeJpaRepository.findById(storeId)
//                .orElseThrow(() -> new IllegalArgumentException("Store not found for id: " + storeId));
        GroupEntity groupEntity = GroupEntity.fromGroupDomain(group);
        return groupJpaRepository.save(groupEntity).getId();
    }

    @Override
    public Optional<Group> findGroupById(Long id) {
        return groupJpaRepository.findById(id).map(GroupEntity::toGroupDomain);
    }

    @Override
    public List<Group> findAllGroups(String category, String regionCode, String storeName, int page) {
        return groupJpaRepository.findAll().stream()
                .map(GroupEntity::toGroupDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void updateGroupParticipants(Long groupId, int participants) {
        GroupEntity groupEntity = groupJpaRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found for id: " + groupId));
//        groupEntity = GroupEntity.builder()
//                .title(groupEntity.getTitle())
//                .content(groupEntity.getContent())
//                .maxParticipants(groupEntity.getMaxParticipants())
//                .minParticipants(groupEntity.getMinParticipants())
//                .currentParticipants(participants)
//                .meetingDate(groupEntity.getMeetingDate())
//                .meetingStatus(groupEntity.getMeetingStatus())
//                .storeId(groupEntity.getStoreId())
//                .build();
//        groupJpaRepository.save(groupEntity);
        groupEntity.updateCurrentCount(participants);
    }
}