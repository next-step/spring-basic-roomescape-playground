package roomescape.waiting;

public class WaitingRequest {

    private final String date;
    private final Long time;
    private final Long theme;

    public WaitingRequest(String date, Long time, Long theme) {
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public String getDate(){ return date; }

    public Long getTime(){ return time; }

    public Long getTheme(){ return theme; }
}
