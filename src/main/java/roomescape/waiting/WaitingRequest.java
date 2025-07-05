package roomescape.waiting;

public record WaitingRequest(
        String name,
        String date,
        Long themeId,
        Long timeId
) {
    public WaitingRequest {
        if (!StringUtils.hasText(date)) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "날짜는 필수입니다");
        }
        if (theme == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "테마는 필수입니다");
        }
        if (time == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "테마는 필수입니다");
        }
    }

    public WaitingRequest withDefaultName(String defaultName) {
        return new WaitingRequest(defaultName, date, theme, time);
    }
}
