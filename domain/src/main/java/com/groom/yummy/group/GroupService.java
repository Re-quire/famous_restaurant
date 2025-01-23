package com.groom.yummy.group;

import com.groom.yummy.exception.CustomException;
import com.groom.yummy.exception.GroupErrorCode;
import com.groom.yummy.exception.StoreErrorCode;
import com.groom.yummy.exception.UserErrorCode;
import com.groom.yummy.store.Store;
import com.groom.yummy.store.StoreRepository;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserRepository;
import com.groom.yummy.usertogroup.AttendanceStatus;
import com.groom.yummy.usertogroup.UserToGroup;
import com.groom.yummy.usertogroup.UserToGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserToGroupRepository userToGroupRepository;
    private final StoreRepository storeRepository;

    public Long createGroup(Long storeId, Long userId, String title, String content, Integer maxParticipants, Integer minParticipants, LocalDateTime meetingDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        if (storeId == null) {
            throw new CustomException(StoreErrorCode.STORE_NOT_FOUND);
        }

        Group group = Group.builder()
                .title(title)
                .content(content)
                .maxParticipants(maxParticipants)
                .minParticipants(minParticipants)
                .currentParticipants(1)
                .meetingDate(meetingDate)
                .meetingStatus(MeetingStatus.OPEN)
                .storeId(storeId)
                .build();

        try {
            Long groupId = groupRepository.saveGroup(group);
            userToGroupRepository.saveUserToGroup(groupId, user.getId(), true, AttendanceStatus.APPROVED);
            return groupId;
        } catch (Exception e) {
            throw new CustomException(GroupErrorCode.GROUP_CREATE_FAILED);
        }
    }

    public Optional<Group> findGroupById(Long id) {
        return groupRepository.findGroupById(id);
    }

    public List<Group> getAllGroups(String category, String regionCode, String storeName, int page) {
        return groupRepository.findAllGroups(category, regionCode, storeName, page);
    }

    public void joinGroup(Long groupId, Long userId, Long storeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        Group group = groupRepository.findGroupById(groupId)
                .orElseThrow(() -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND));

//        Store store = storeRepository.findStoreById(storeId)
//                .orElseThrow(() -> new CustomException(StoreErrorCode.STORE_NOT_FOUND));

        if (group.getCurrentParticipants() >= group.getMaxParticipants()) {
            throw new CustomException(GroupErrorCode.GROUP_PARTICIPATION_FULL);
        }
        groupRepository.updateGroupParticipants(groupId, group.getCurrentParticipants() + 1);

        userToGroupRepository.saveUserToGroup(group.getId(), user.getId(), false, AttendanceStatus.APPROVED);
    }

}

