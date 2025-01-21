package com.groom.yummy.user.event;

public record UserNicknameChangedEvent(
        Long userId,
        String newNickname
){}
