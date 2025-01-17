package com.groom.yummy.user;

public record UserNicknameChangedEvent(
        Long userId,
        String newNickname
){}
