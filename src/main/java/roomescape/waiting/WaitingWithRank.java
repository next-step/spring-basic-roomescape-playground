package roomescape.waiting;

public class WaitingWithRank {
    private Waiting waiting;
    private int rank;

    public WaitingWithRank(Waiting waiting, int rank) {
        this.waiting = waiting;
        this.rank = rank;
    }

    public Waiting getWaiting() {
        return waiting;
    }

    public int getRank() {
        return rank;
    }
}
