package roomescape.waiting;

public class WaitingRanking {

    private final Waiting waiting;
    private final long rank;

    public WaitingRanking(Waiting waiting, long rank) {
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
