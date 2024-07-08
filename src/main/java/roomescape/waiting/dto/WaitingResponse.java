package roomescape.waiting.dto;

public record WaitingResponse(
        Long id,
        String date,
        String time,
        String theme,
        Long rank
) {
}
