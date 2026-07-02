package roomescape.reservation;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationMineResponse {
    private Long reservationId;
    private String theme;
    private String date;
    private String time;
    private String status;

    public ReservationMineResponse() {
    }

    public ReservationMineResponse(Long reservationId, String theme, String date, String time, String status) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    @JsonProperty("reservationId")
    public Long getReservationId() {
        return reservationId;
    }

    @JsonProperty("id")
    public Long getId() {
        return reservationId;
    }

    @JsonAlias("id")
    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
