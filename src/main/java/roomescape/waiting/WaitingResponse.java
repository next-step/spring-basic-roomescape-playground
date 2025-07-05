package roomescape.waiting;

public record WaitingResponse(
        Long id,
        String name,
        String date,
        String theme,
        String time,
        Long waitingNumber
) {

    public static WaitingResponse from(Waiting waiting, Long waitingNumber) {
        return new WaitingResponse(
                waiting.getId(),
                waiting.getName(),
                waiting.getDate(),
                waiting.getTheme().getName(),
                waiting.getTime().getValue(),
                waitingNumber);
    }

    public static WaitingResponse from(WaitingWithRank waitingWithRank) {
        Waiting waiting = waitingWithRank.getWaiting();
        return new WaitingResponse(
                waiting.getId(),
                waiting.getName(),
                waiting.getDate(),
                waiting.getTheme().getName(),
                waiting.getTime().getValue(), waitingWithRank.getRank()
        );
    }
}
