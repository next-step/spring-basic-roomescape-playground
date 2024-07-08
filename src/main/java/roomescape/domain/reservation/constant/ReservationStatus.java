package roomescape.domain.reservation.constant;

public enum ReservationStatus {

    RESERVED("예약"),
    WAITING("%d번째 예약대기");

    private String message;

    ReservationStatus(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage(Long number) {
        return String.format(message, number);
    }
}
