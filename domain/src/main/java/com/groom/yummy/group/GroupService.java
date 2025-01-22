package com.groom.yummy.group;

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
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Long storeId = group.getStoreId();
        if (storeId == null) {
            throw new IllegalArgumentException("Store ID는 null일 수 없습니다.");
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
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Group group = groupRepository.findGroupById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("소모임을 찾을 수 없습니다."));

        if (group.getCurrentParticipants() >= group.getMaxParticipants()) {
            throw new IllegalStateException("참가 인원이 가득 찼습니다.");
        }
        groupRepository.updateGroupParticipants(groupId, group.getCurrentParticipants() + 1);

        userToGroupRepository.save(group, user, false, AttendanceStatus.APPROVED);
    }

}

