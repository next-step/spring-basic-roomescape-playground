package roomescape.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record MyReservationResponse(
        Long reservationId,
        String theme,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        String date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime time,
        String status
) {

    public MyReservationResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                LocalTime.parse(reservation.getTime().getValue()),
                "예약"
        );
    }
}
