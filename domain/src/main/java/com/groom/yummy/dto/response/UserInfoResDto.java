package com.groom.yummy.dto.response;

import com.groom.yummy.user.User;
import lombok.Builder;

@Builder
public record UserInfoResDto(
        Long id,
        String email,
        String nickname,
        Long joinCount,
        Long participationCount
) {
    public static UserInfoResDto from(User user){
        return UserInfoResDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .joinCount(user.getGroupJoinCount())
                .participationCount(user.getGroupAttendanceCount())
                .build();
    }
}
