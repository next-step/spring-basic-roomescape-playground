package roomescape.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationResponse {

    private Long id;
    private String name;
    private String theme;
    private String date;
    private String time;
}
