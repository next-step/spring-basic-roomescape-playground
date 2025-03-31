package roomescape.waiting.dto;

public class WaitingRequest {
    private String date;
    private Long time;
    private Long theme;

    public WaitingRequest(String date, Long time, Long theme) {
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

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
