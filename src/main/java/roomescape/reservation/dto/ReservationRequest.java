package roomescape.reservation.dto;

import lombok.Getter;

@Getter
public class ReservationRequest {

    private String name;
    private String date;
    private Long themeId;
    private Long timeId;
}
