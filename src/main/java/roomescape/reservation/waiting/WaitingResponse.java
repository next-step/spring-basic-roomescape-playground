package roomescape.reservation.waiting;

public record WaitingResponse(
	Long waitingId,
	String name,
	String theme,
	String date,
	String time
) {
}
