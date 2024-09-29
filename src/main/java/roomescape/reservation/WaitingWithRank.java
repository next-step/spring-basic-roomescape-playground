package roomescape.reservation;

public record WaitingWithRank(
    Reservation reservation,
    Long rank
) {
}
