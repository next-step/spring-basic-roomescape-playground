package roomescape.reservation;

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
    private String status;

    public ReservationResponse(Long Id, String theme, String date, String time, String status) {
        this.id = Id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

}