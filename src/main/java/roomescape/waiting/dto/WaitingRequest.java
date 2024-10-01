package roomescape.waiting.dto;

public record WaitingRequest(
    String name,
    Long theme,
    String date,
    Long time
) {
}
