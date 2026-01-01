package roomescape.reservation.waiting;

public record WaitingResponse(
        Long id,
        String theme,
        String date,
        String time
) {
    public static WaitingResponse from(Waiting waiting) {
        return new WaitingResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue()
        );
    }
}
