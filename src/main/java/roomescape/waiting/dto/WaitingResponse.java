package roomescape.waiting.dto;

public record WaitingResponse(
    Long id,
    String name,
    String theme,
    String date,
    String time,
    Long waitingNumber
) {
}
