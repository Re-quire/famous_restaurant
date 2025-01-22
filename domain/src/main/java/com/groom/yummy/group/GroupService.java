package com.groom.yummy.group;

import com.groom.yummy.exception.CustomException;
import com.groom.yummy.exception.GroupErrorCode;
import com.groom.yummy.exception.StoreErrorCode;
import com.groom.yummy.exception.UserErrorCode;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserRepository;
import com.groom.yummy.usertogroup.AttendanceStatus;
import com.groom.yummy.usertogroup.UserToGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserToGroupRepository userToGroupRepository;

    public Long createGroup(Group group, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        Long storeId = group.getStoreId();
        if (storeId == null) {
            throw new CustomException(StoreErrorCode.STORE_NOT_FOUND);
        }
        return groupRepository.saveGroup(group, storeId);
    }

    public Optional<Group> findGroupById(Long id) {
        return groupRepository.findGroupById(id);
    }

    public List<Group> getAllGroups(String category, String regionCode, String storeName, int page) {
        return groupRepository.findAllGroups(category, regionCode, storeName, page);
    }

    public void joinGroup(Long groupId, Long userId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        Group group = groupRepository.findGroupById(groupId)
                .orElseThrow(() -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND));

        if (group.getCurrentParticipants() >= group.getMaxParticipants()) {
            throw new CustomException(GroupErrorCode.GROUP_PARTICIPATION_FULL);
        }
        groupRepository.updateGroupParticipants(groupId, group.getCurrentParticipants() + 1);

        userToGroupRepository.save(group, user, false, AttendanceStatus.APPROVED);
    }

}

