package com.groom.yummy.group;

import lombok.Getter;

@Getter
public enum MeetingStatus {
    OPEN("OPEN", "모집 중: 참가 요청을 받고 있는 상태"),
    CLOSED("CLOSED", "모집 마감: 더 이상 참가 요청을 받지 않는 상태."),
    FINALIZED("FINALIZED", "팀 결성 완료: 모집이 끝나고 팀이 확정된 상태"),
    CANCELLED("CANCELLED", "모집 취소: 소모임 자체가 취소된 상태"),
    IN_PROGRESS("IN_PROGRESS", "모임 진행 중: 팀 결성 후 모임이 진행 중인 상태"),
    COMPLETED("COMPLETED", "모임 완료: 모임이 종료된 상태");

    private final String code;
    private final String description;

    MeetingStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", code, description);
    }

    public static MeetingStatus fromCode(String code) {
        for (MeetingStatus status : MeetingStatus.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown MeetingStatus code: " + code);
    }
}
