package com.groom.yummy.domain.usertogroup;

import com.groom.yummy.domain.BaseEntity;
import com.groom.yummy.domain.group.GroupEntity;
import com.groom.yummy.domain.user.UserEntity;
import com.groom.yummy.usertogroup.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_groups")
@SuperBuilder
@Getter
public class UserToGroupEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isLeader;

    @Getter
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

    public static UserToGroupEntity create(GroupEntity groupEntity, UserEntity userEntity, boolean isLeader) {
        return UserToGroupEntity.builder()
                .group(groupEntity)
                .user(userEntity)
                .isLeader(isLeader)
                .attendanceStatus(AttendanceStatus.APPROVED)
                .build();
    }

}
