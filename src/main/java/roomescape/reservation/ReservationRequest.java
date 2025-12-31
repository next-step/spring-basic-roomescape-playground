package roomescape.reservation;

import jakarta.validation.constraints.NotNull;

public class ReservationRequest {
    private String name;
    @NotNull
    private String date;
    @NotNull
    private Long theme;
    @NotNull
    private Long time;

    public ReservationRequest() {
    }

    public ReservationRequest(String name, String date, Long theme, Long time) {
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Long getTheme() {
        return theme;
    }

    public Long getTime() {
        return time;
    }
}
