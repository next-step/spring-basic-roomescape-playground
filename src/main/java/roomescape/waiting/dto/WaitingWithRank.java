package roomescape.waiting.dto;

import roomescape.waiting.model.Waiting;

public record WaitingWithRank(
    Waiting waiting,
    Long rank
) {
}
