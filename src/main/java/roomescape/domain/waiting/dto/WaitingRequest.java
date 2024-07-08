package roomescape.domain.waiting.dto;

public record WaitingRequest(
        String date,
        Long time,
        Long theme
) {
}
