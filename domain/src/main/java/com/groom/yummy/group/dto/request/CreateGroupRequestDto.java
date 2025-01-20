package com.groom.yummy.group.dto.request;

import com.groom.yummy.group.Group;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGroupRequestDto {
    private String title;
    private String content;
    private Integer maxParticipants;
    private Integer minParticipants;
    private LocalDateTime meetingDate;
    private Long storeId;

    public Group toGroupDomain() {
        return Group.builder()
                .title(this.title)
                .content(this.content)
                .maxParticipants(this.maxParticipants)
                .minParticipants(this.minParticipants)
                .currentParticipants(1)
                .meetingDate(this.meetingDate)
                .storeId(this.storeId)
                .build();
    }
}

