package roomescape.waiting.dto;

import roomescape.waiting.model.Waiting;
import roomescape.waiting.model.WaitingWithRank;

public record WaitingResponse(
    Long id,
    String name,
    String theme,
    String date,
    String time,
    Long waitingNumber
) {
    public static WaitingResponse from(WaitingWithRank waitingWithRank) {
        Waiting waiting = waitingWithRank.getWaiting();
        Long rank = waitingWithRank.getRank();
        return new WaitingResponse(
            waiting.getId(),
            waiting.getName(),
            waiting.getTheme().getName(),
            waiting.getDate(),
            waiting.getTime().getTime(),
            rank
        );
    }
}
