package roomescape.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyReservationsResponse {

    private Long reservationId;
    private String theme;
    private String date;
    private String time;
    private String status;
}
