package roomescape.waiting;

public class WaitingResponse {
    private Long id;
    private String date;
    private String time;
    private String theme;
    private Long memberId;
    private int rank;

    public WaitingResponse(Long id, String date, String time, String theme, Long memberId, int rank) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.memberId = memberId;
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTheme() {
        return theme;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getRank() {
        return rank;
    }
}
