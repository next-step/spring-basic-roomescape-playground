package roomescape.reservation;

import lombok.Getter;

@Getter
public class ReservationMineResponse {
    private final Long id;
    private final String theme;
    private final String date;
    private final String time;
    private final String status;

    public ReservationMineResponse(Long id, String theme, String date, String time, String status) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }
}
