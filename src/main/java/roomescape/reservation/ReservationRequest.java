package roomescape.reservation;

public class ReservationRequest {
    private String name;
    private Long memberId;
    private final String date;
    private final Long theme;
    private final Long time;

    public ReservationRequest(String date, Long theme, Long time) {
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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
}
