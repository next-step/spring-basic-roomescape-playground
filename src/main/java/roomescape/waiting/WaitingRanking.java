package roomescape.waiting;

public class WaitingRanking {

    private final Waiting waiting;
    private final Long rank;

    public WaitingRanking(Waiting waiting, Long rank) {
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
