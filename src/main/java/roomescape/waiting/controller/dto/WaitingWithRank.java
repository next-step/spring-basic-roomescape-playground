package roomescape.waiting.controller.dto;

import roomescape.waiting.domain.Waiting;

public class WaitingWithRank {
    private Waiting waiting;
    private Long rank;

    public WaitingWithRank(Waiting waiting, Long rank) {
        this.waiting = waiting;
        this.rank = rank;
    }

    public Waiting getWaiting() {
        return waiting;
    }

    public Long getRank() {
        return rank;
    }
}