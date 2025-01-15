package com.groom.yummy.domain.user;

import com.groom.yummy.domain.BaseEntity;
import com.groom.yummy.domain.usertogroup.UserToGroupEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@SuperBuilder
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private Long groupJoinCount;

    @Column(nullable = false)
    private Long groupAttendanceCount;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserToGroupEntity> userGroups;

    private UserEntity(String email, String nickname){
        this.email = email;
        this.nickname = nickname;
        this.groupJoinCount = 0L;
        this.groupAttendanceCount = 0L;
        this.isDeleted = false;
    }

    public static UserEntity createNewUser(String email, String nickname) {
        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Email must not be null or empty");
        }

        if (nickname == null || nickname.isBlank()) {
            throw new IllegalStateException("nickname must not be null or empty");
        }
        return new UserEntity(email, nickname);
    }
}
