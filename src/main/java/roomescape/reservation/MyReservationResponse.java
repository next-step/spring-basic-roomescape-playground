package roomescape.reservation;

import java.util.List;
import java.util.stream.Collectors;

public record MyReservationResponse(Long reservationId,
                                    String theme,
                                    String date,
                                    String time,
                                    String status) {

    public static List<MyReservationResponse> from(List<ReservationResponse> reservations) {
        return reservations.stream()
                           .map(it -> new MyReservationResponse(it.id(),
                                                                it.theme(),
                                                                it.date(),
                                                                it.time(),
                                                                "예약"))
                           .collect(Collectors.toList());
    }
}
