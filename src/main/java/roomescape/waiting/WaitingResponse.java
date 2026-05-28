package roomescape.waiting;

public record WaitingResponse(
        Long id,
        String theme,
        String date,
        String time
) {
    public Long getId() {
        return id;
    }
}