package com.groom.yummy.group.dto.response;

import com.groom.yummy.group.Group;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupResponseDto {
    private Long id;
    private String title;
    private String content;
    private Integer maxParticipants;
    private Integer minParticipants;
    private Integer currentParticipants;
    private LocalDateTime meetingDate;
    private String meetingStatus;
    private Long storeId;
    private LocalDateTime createdAt;

    public static GroupResponseDto fromGroupDomain(Group group) {
        return GroupResponseDto.builder()
                .id(group.getId())
                .title(group.getTitle())
                .content(group.getContent())
                .maxParticipants(group.getMaxParticipants())
                .minParticipants(group.getMinParticipants())
                .currentParticipants(group.getCurrentParticipants())
                .meetingDate(group.getMeetingDate())
                .meetingStatus(group.getMeetingStatus().toString())
                .storeId(group.getStoreId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}

