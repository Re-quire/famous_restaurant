package com.groom.yummy.user;

public record UserDeleteEvent(
        Long userId,
        boolean isDeleted
) {}
