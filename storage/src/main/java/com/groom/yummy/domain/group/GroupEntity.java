package com.groom.yummy.domain.group;

import com.groom.yummy.domain.BaseEntity;
import com.groom.yummy.domain.store.StoreEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "teams")
@SuperBuilder
public class GroupEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    private Integer minParticipants;

    @Column(nullable = false)
    private Integer currentParticipants;

    @Column(nullable = false)
    private LocalDateTime meetingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingStatus meetingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @Builder
    public GroupEntity(String title, String content, Integer maxParticipants, Integer minParticipants,
                       Integer currentParticipants, LocalDateTime meetingDate, MeetingStatus meetingStatus, StoreEntity store) {
        this.title = title;
        this.content = content;
        this.maxParticipants = maxParticipants;
        this.minParticipants = minParticipants;
        this.currentParticipants = currentParticipants;
        this.meetingDate = meetingDate;
        this.meetingStatus = meetingStatus;
        this.store = store;
    }
}
