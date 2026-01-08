package roomescape.waiting;

public class WaitingResponse {
    private Long id;
    private String theme;
    private String date;
    private String time;
    private Long rank;

    public WaitingResponse(Long id, String theme, String date, String time, Long rank) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.rank = rank;
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

    public Long getRank() {
        return rank;
    }
}
