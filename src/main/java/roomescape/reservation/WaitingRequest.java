package roomescape.reservation;

import jakarta.validation.constraints.NotNull;

public class WaitingRequest {
    private String name;
    @NotNull
    private String date;
    @NotNull
    private Long theme;
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
