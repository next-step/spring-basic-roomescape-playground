package roomescape.domain.waiting.web.dto;

import roomescape.domain.waiting.service.result.WaitingWithRank;

public record WaitingResponse(
        Long id,
        Long waitingNumber
) {
    public static WaitingResponse from(WaitingWithRank waitingWithRank) {
        return new WaitingResponse(waitingWithRank.reserveWaiting().getId(), waitingWithRank.rank());
    }
}
