package com.groom.yummy.usertogroup;

import com.groom.yummy.group.Group;
import com.groom.yummy.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserToGroup {
    private Long id;
    private Group group;
    private User user;
    private boolean isLeader;
    private AttendanceStatus attendanceStatus;
}
