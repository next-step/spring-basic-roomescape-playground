package roomescape.reservation.dto;

import org.jetbrains.annotations.NotNull;

public class AdminReservationRequest {
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String date;
    @NotNull
    private Long theme;
    @NotNull
    private Long time;

    public AdminReservationRequest(String name, String email, String date, Long theme, Long time) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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
