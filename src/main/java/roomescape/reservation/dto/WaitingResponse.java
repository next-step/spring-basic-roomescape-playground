package roomescape.reservation.dto;

public record WaitingResponse(
        Long id,
        Long waitingNumber
) {
    public Long getId() {
        return id;
    }
}
