package roomescape.reservation.dto;

import lombok.Getter;
import roomescape.reservation.Reservation;

import java.time.format.DateTimeFormatter;

public class MyReservationResponse {

    @Getter
    private final Long reservationId;
    @Getter
    private final String theme;
    @Getter
    private final String date;
    @Getter
    private final String time;
    @Getter
    private final String status;

    public MyReservationResponse(Long reservationId, String theme, String date, String time, String status) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                reservation.getTime().getTime(),
                "예약"
        );
    }
}
