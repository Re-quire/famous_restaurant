package com.groom.yummy.group;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    Optional<Group> findGroupById(Long id);
    Long saveGroup(Group group, Long storeId);
    List<Group> findAllGroups(String category, String regionCode, String storeName, int page);
    void updateGroupParticipants(Long groupId, int newParticipantCount);
}
