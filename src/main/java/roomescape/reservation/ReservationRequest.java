package roomescape.reservation;

public class ReservationRequest {
    private String name;
    private String date;
    private Long theme;
    private Long time;

    public ReservationRequest(String name, String date, Long theme, Long time) {
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public ReservationRequest {
        if (!StringUtils.hasText(date)) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "날짜는 필수입니다.");
        }
        if (theme == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "테마는 필수입니다.");
        }
        if (time == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "시간은 필수입니다.");
        }
    }

    public Long getTime() {
        return time;
    }
}
