package roomescape.reservation.model;

public enum ReservationStatus {
    PENDING("대기"),
    CONFIRMED("예약");

    private String status;

    ReservationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPendingStatus(Long rank) {
        if (!this.equals(PENDING)) return;

        this.status = String.valueOf(rank) + "번째로 예약대기";
    }

    @Override
    public String toString() {
        return status;
    }
}
