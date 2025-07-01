package roomescape.exception;

public class TimeNotFoundException extends RuntimeException {
    public TimeNotFoundException() {
        super("해당 시간이 존재하지 않습니다.");
    }
}
