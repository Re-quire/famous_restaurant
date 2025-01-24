package com.groom.yummy.usertogroup;

import com.groom.yummy.group.Group;
import com.groom.yummy.user.User;

import java.util.Optional;

public interface UserToGroupRepository {
    void saveUserToGroup(Long groupId, Long userId, boolean isLeader, AttendanceStatus attendanceStatus);
    Optional<UserToGroup> findByGroupAndUser(Group group, User user);
}


