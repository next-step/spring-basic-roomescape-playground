package roomescape.waiting;

public record WaitingResponse(
        Long id,
        String name,
        String theme,
        String date,
        String time
) {

    public static WaitingResponse from(Waiting waiting) {
        return new WaitingResponse(
                waiting.getId(),
                waiting.getName(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue()
        );
    }
}
