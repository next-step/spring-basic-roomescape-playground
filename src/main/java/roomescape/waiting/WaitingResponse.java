package roomescape.waiting;

public record WaitingResponse(
        Long id,
        String theme,
        String time,
        String date,
        int waitingNum
) {
}