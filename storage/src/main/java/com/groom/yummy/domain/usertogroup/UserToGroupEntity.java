package com.groom.yummy.domain.usertogroup;

import com.groom.yummy.domain.BaseEntity;
import com.groom.yummy.domain.group.GroupEntity;
import com.groom.yummy.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_groups")
@SuperBuilder
public class UserToGroupEntity extends BaseEntity {

    @Column(nullable = false)
    private boolean isLeader;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus attendanceStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @Builder
    public UserToGroupEntity(AttendanceStatus attendanceStatus, boolean isLeader, UserEntity user, GroupEntity group) {
        this.attendanceStatus = attendanceStatus;
        this.isLeader = isLeader;
        this.user = user;
        this.group = group;
    }
}
