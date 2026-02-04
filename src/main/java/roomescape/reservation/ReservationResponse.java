package roomescape.reservation;

import lombok.Getter;

@Getter
public class ReservationResponse {
    private Long id;
    private String name;
    private String theme;
    private String date;
    private String time;

    public ReservationResponse(Long id, String name, String theme, String date, String time) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }
}
