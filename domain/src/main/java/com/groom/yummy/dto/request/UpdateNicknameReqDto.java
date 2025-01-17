package com.groom.yummy.dto.request;

import lombok.Builder;

@Builder
public record UpdateNicknameReqDto(
        String nickname
){}