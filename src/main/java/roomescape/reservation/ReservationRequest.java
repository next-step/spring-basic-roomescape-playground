package roomescape.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationRequest {
    private String name;
    @NotBlank
    private String date;
    @NotNull
    private Long theme = 1L;
    @NotNull
    private Long time;

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

    public void setName(String name) {
        this.name = name;
    }

}
