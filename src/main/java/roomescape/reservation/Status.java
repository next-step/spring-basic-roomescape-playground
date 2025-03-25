package roomescape.reservation;

public enum Status {
    RESERVATION("예약"),
    WAIT("예약대기");

    private String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
