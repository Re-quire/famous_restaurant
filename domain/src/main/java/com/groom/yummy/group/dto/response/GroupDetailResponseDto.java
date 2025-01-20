package com.groom.yummy.group.dto.response;

import com.groom.yummy.group.Group;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDetailResponseDto{

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

    private StoreDetailResponseDto storeDetailResponse;
    private List<ParticipantResponseDto> participantResponseList;

    public static GroupDetailResponseDto fromGroupDomain(Group group) {
        return GroupDetailResponseDto.builder()
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

