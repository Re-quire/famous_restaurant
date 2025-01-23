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
    private Long storeId;
    private String title;
    private String content;
    private Integer maxParticipants;
    private Integer minParticipants;
    private LocalDateTime meetingDate;

    public Group toGroupDomain() {
        return Group.builder()
                .title(this.title)
                .content(this.content)
                .maxParticipants(this.maxParticipants)
                .minParticipants(this.minParticipants)
                .meetingDate(this.meetingDate)
                .storeId(this.storeId)
                .build();
    }
}

