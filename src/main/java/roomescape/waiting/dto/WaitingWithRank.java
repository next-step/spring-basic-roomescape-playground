package roomescape.waiting.dto;

import lombok.Getter;
import roomescape.waiting.Waiting;

public class WaitingWithRank {

    @Getter
    private final Waiting waiting;
    @Getter
    private final Long rank;

    public WaitingWithRank(Waiting waiting, Long rank) {
        this.waiting = waiting;
        this.rank = rank;
    }
}
