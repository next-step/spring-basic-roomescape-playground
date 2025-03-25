package roomescape.reservation;

public enum Status {
    RESERVATION("예약");

    private String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
