package roomescape.reservation;

import lombok.Builder;
import lombok.Data;
import roomescape.theme.Theme;

@Data
@Builder
public class MyReservationResponse {
    private Long reservationId;
    private String theme;
    private String date;
    private String time;
    private String status;
}
