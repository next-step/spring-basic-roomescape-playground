package roomescape.reservation.dto.response;

import roomescape.reservation.domain.Waiting;

public record WaitingResponse(
    Long id,
    String name,
    String theme,
    String date,
    String time,
    Integer waitingNumber
) {
    public static WaitingResponse from(Waiting waiting, Integer waitingNumber) {
        return new WaitingResponse(
            waiting.getId(),
            waiting.getName(),
            waiting.getTheme().getName(),
            waiting.getDate(),
            waiting.getTime().getTime(),
            waitingNumber
        );
    }
}
