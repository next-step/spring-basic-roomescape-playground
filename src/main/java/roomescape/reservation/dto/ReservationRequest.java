package roomescape.reservation.dto;

import org.jetbrains.annotations.NotNull;

public class ReservationRequest {
    @NotNull
    private String name;
    @NotNull
    private String date;
    @NotNull
    private Long theme;
    @NotNull
    private Long time;

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
