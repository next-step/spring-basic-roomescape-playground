package roomescape.waiting;

public record WaitingResponse(
        Long id,
        String name,
        String date,
        String time,
        String theme
) {
    public static WaitingResponse from(Waiting waiting) {
        return new WaitingResponse(waiting.getId(), waiting.getName(), waiting.getDate(), waiting.getTime().getValue(),
                waiting.getTheme().getName());
    }
}
