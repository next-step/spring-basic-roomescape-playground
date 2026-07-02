package roomescape.reservation;

public class ReservationResponse {
    private Long id;
    private String name;
    private Long memberId;
    private String theme;
    private String date;
    private String time;

    public ReservationResponse(Long id, String name, Long memberId, String theme, String date, String time) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMemberId() {
        return memberId;
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
