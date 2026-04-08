package com.vaporlabs.chat.enums;

public enum LobbyTTL {
    ONE_HOUR(3600L),
    TWO_HOURS(7200L),
    THREE_HOURS(10800L),
    TWELVE_HOURS(43200L),
    ONE_DAY(86400L);

    private final Long seconds;

    LobbyTTL(Long seconds) {
        this.seconds = seconds;
    }

    public Long getSeconds() {
        return seconds;
    }
}
