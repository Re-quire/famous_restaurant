package com.groom.yummy.domain.user;

import com.groom.yummy.domain.BaseEntity;
import com.groom.yummy.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@SuperBuilder
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Long groupJoinCount;

    @Column(nullable = false)
    private Long groupAttendanceCount;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private String role;

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void deleteUser(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    public static User toModel(UserEntity userEntity){
        return User.builder()
                .id(userEntity.getId())
                .nickname(userEntity.nickname)
                .email(userEntity.getEmail())
                .role(userEntity.role)
                .groupAttendanceCount(userEntity.groupAttendanceCount)
                .groupJoinCount(userEntity.groupJoinCount)
                .isDeleted(userEntity.isDeleted)
                .build();
    }

    public static UserEntity toEntity(User user){
        return UserEntity.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole())
                .groupAttendanceCount(user.getGroupAttendanceCount())
                .groupJoinCount(user.getGroupJoinCount())
                .isDeleted(user.isDeleted())
                .build();
    }
}
