package com.groom.yummy.group.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantResponseDto {
    private Long userId;
    private String nickname;
    private String attendanceStatus;
    private Boolean isLeader;
}
