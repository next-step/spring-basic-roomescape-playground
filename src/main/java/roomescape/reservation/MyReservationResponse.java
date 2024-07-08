package roomescape.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyReservationResponse {
    private Long id;
    private String theme;
    private String date;
    private String time;
    private String status;

    public static MyReservationResponse from(final Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getEvent_value(),
                "예약"
        );
    }
}