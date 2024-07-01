package roomescape.waiting;

public record WaitingResponse(Long id,
                              String name,
                              String theme,
                              String date,
                              String time,
                              Long waitingNumber) {
}
