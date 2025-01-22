package com.groom.yummy.user.event;

public record UserDeleteEvent(
        Long userId,
        boolean isDeleted
) {}
