package roomescape.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import roomescape.reservation.Reservation;

@Getter
@AllArgsConstructor
public class ReservationResponse {

    private Long id;
    private String name;
    private String theme;
    private String date;
    private String time;

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getTheme().getName(),
                reservation.getDate().toString(),
                reservation.getTime().getTime()
        );
    }
}
