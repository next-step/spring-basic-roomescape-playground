package roomescape.reservation.controller.dto;

public record WaitingRequest(
    String date,
    Long theme,
    Long time
) {
}
