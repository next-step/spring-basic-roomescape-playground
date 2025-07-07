package roomescape.waiting;

import java.time.LocalDate;

public record WaitingResponse(
        Long id,
        String name,
        LocalDate date,
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
