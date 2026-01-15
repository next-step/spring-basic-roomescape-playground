package roomescape.waiting;

public record WaitingResponse(
        Long id,
        String theme,
        String date,
        String time,
        Long rank
) {

    public static WaitingResponse from(Waiting waiting, Long rank) {
        return new WaitingResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue(),
                rank
        );
    }
}
