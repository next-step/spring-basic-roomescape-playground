package roomescape.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MyReservationResponse {

    private Long reservationId;
    private String theme;
    private String date;
    private String time;
    private String status;
}
