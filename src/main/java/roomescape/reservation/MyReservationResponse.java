package roomescape.reservation;

public record MyReservationResponse(
    Long reservationId,
    String theme,
    String date,
    String time,
    String status
) {

    public static MyReservationResponse from(Reservation reservation, Integer rank) {
        return new MyReservationResponse(
            reservation.getId(),
            reservation.getTheme().getName(),
            reservation.getDate(),
            reservation.getTime().getValue(),
            getStatus(reservation, rank)
        );
    }

    public static String getStatus(Reservation reservation, Integer rank) {
        if (reservation.isWaiting()) {
            return rank + "번째 예약대기";
        }
        return "예약";
    }
}
