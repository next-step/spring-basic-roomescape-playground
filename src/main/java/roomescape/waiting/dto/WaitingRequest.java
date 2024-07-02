package roomescape.waiting.dto;


public record WaitingRequest(
        String date,
        Long time,
        Long theme
) {
}