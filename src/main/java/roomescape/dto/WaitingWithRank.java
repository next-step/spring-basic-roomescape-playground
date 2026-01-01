package roomescape.dto;

import roomescape.model.Waiting;

public record WaitingWithRank(
        Waiting waiting,
        Long rank
) { }
