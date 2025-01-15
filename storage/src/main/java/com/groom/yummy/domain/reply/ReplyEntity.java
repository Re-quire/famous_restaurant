package com.groom.yummy.domain.reply;

import com.groom.yummy.domain.BaseEntity;
import com.groom.yummy.domain.group.GroupEntity;
import com.groom.yummy.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name="reply")
@SuperBuilder
public class ReplyEntity extends BaseEntity {

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ReplyEntity parentReply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @Builder
    public ReplyEntity(String content, ReplyEntity parentReply, UserEntity user, GroupEntity group) {
        this.content = content;
        this.parentReply = parentReply;
        this.user = user;
        this.group = group;
    }
}
