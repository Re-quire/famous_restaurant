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

    protected void changeNickname(String nickname){
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new RuntimeException("닉네임은 null이거나 빈 값일 수 없습니다.");
        }
        this.nickname = nickname;
    }

    protected void deleteUser(){
        this.isDeleted = true;
    }
}
