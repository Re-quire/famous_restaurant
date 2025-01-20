package com.groom.yummy.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    private Long id;
    private String title;
    private String content;
    private Integer maxParticipants;
    private Integer minParticipants;
    private Integer currentParticipants;
    private LocalDateTime meetingDate;
    private MeetingStatus meetingStatus;
    private Long storeId;
}
