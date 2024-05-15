package roomescape.reservation;

import org.jetbrains.annotations.NotNull;

public class ReservationRequest {
    @NotNull
    private String date;

    @NotNull
    private Long theme;

    @NotNull
    private Long time;

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
