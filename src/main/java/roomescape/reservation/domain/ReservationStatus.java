package roomescape.reservation.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatus {
    RESERVATION("예약"),
    WAITING("대기"),
    CANCELLED("취소");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}