package roomescape.waiting.domain;

public class RankedWaiting {
    private Waiting waiting;
    private Long rank;

    public RankedWaiting(Waiting waiting, Long rank) {
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
