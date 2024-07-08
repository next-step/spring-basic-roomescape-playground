package roomescape.domain.waiting.dto;

import roomescape.domain.waiting.entity.Waiting;

public record WaitingResponse(
        Long id,
        String theme,
        String date,
        String time,
        Long waitingNumber
) {

    public static WaitingResponse of(final Waiting waiting,
                                     final Long waitingNumber) {
        return new WaitingResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue(),
                waitingNumber
        );
    }
}
