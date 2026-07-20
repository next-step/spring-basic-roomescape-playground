package roomescape.waiting;

public class WaitingResponse {
    private final Long id;
    private final String theme;
    private final String date;
    private final String time;

    public WaitingResponse(Long id, String theme, String date, String time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

}
