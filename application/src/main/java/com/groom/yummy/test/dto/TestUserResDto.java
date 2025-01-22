package com.groom.yummy.test.dto;

import com.groom.yummy.user.User;
import lombok.Builder;

@Builder
public record TestUserResDto(
        Long id,
        String email,
        String nickname,
        Long joinCount,
        Long participationCount
) {
    public static TestUserResDto from(User user){
        return TestUserResDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .joinCount(user.getGroupJoinCount())
                .participationCount(user.getGroupAttendanceCount())
                .build();
    }
}
