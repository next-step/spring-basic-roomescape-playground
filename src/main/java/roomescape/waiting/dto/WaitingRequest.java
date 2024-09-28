package roomescape.waiting.dto;

public record WaitingRequest(
    String date,
    Long theme,
    Long time
) {
}
