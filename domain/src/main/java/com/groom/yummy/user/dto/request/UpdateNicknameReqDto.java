package com.groom.yummy.user.dto.request;

import lombok.Builder;

@Builder
public record UpdateNicknameReqDto(
        String nickname
){}