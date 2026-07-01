package roomescape.reservation;

public enum ReservationStatus {
    PENDING("진행중"),
    CONFIRMED("예약"),
    CANCELLED("취소"),
    FAILED("실패");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
