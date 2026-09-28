package roomescape.reservation;

public record ReservationRequest(String date, Long theme, Long time, Long memberId) {
}