package com.groom.yummy.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String nickname;
    private String role;
    private String email;
    private Long groupJoinCount;
    private Long groupAttendanceCount;
    private boolean isDeleted;
}
