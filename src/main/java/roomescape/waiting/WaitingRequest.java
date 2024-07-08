package roomescape.waiting;

import lombok.Getter;

public class WaitingRequest {
    private String date;
    private Long time;
    private Long theme;

    public String getDate() {
        return date;
    }

    public Long getTime() {
        return time;
    }

    public Long getTheme() {
        return theme;
    }
}