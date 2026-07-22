package roomescape.waiting.dto;

import roomescape.waiting.entity.Waiting;

public record WaitingResponse(
        Long id,
        Long waitingNumber
) {

    public static WaitingResponse of(Waiting waiting, long existingCount) {
        long rank = existingCount + 1;
        return new WaitingResponse(waiting.getId(), rank);
    }
}
