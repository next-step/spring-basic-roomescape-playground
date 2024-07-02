package roomescape.waiting;

public record WaitingRequest(
        String date,
        Long time,
        Long theme
) {
}