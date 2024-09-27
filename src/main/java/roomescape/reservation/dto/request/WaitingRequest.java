package roomescape.reservation.dto.request;

public record WaitingRequest (
    String date,
    Long time,
    Long theme
){

}
