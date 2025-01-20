package roomescape.waiting;

public class WaitingWithRank {
    private Waiting waiting;
    private Long rank;

    public WaitingWithRank(Waiting waiting, Long rank) {
        this.waiting = waiting;
        this.rank = rank;
    }

    // Getter & Setter
    public Waiting getWaiting() {
        return waiting;
    }

    public void setWaiting(Waiting waiting) {
        this.waiting = waiting;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }
}

