package roomescape.exception;

public class DuplicateReservationException extends RuntimeException {
    public DuplicateReservationException() {
        super("이미 예약이 존재합니다");
    }
}
