package roomescape.reservation.dto;

import lombok.Getter;

@Getter
public class ReservationRequest {

    private String name;
    private String date;
    private Long theme;
    private Long time;
}
