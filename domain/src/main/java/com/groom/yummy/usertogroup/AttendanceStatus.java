package com.groom.yummy.usertogroup;

public enum AttendanceStatus {
    ATTENDED("참석"),
    NO_SHOW("불참"),
    APPROVED("승인됨");

    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
