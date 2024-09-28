package roomescape.reservation;

public class WaitingWithRank {

    private Reservation reservation;
    private Long rank;

    protected WaitingWithRank() {}

    public WaitingWithRank(Reservation reservation, Long rank) {
        this.reservation = reservation;
        this.rank = rank;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Long getRank() {
        return rank;
    }
}
