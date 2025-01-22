package com.groom.yummy.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateNicknameReqDto(

        @Schema(description = "닉네임", example = "홍길동")
        @NotBlank
        String nickname
){}