package roomescape.waiting;

public class WaitingWithRank {

    private final Long id;
    private final String theme;
    private final String date;
    private final String time;
    private final Long rank;

    public WaitingWithRank(Long id, String theme, String date, String time, Long rank) {
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
