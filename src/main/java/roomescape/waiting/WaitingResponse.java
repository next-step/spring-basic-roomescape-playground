package roomescape.waiting;

public record WaitingResponse(
        Long id,
        String date,
        String time,
        String theme
) {
    public Long getId() {
        return id;
    }
}
