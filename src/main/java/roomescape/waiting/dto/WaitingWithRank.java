package roomescape.waiting.dto;

import roomescape.waiting.entity.Waiting;

public record WaitingWithRank(
        Waiting waiting,
        Long rank
) {
}

