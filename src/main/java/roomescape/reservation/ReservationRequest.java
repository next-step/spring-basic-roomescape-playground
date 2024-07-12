package roomescape.reservation;

public class ReservationRequest {
    private String name;
    private String date;
    private Long theme;
    private Long time;
    private Long memberId;

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

    public long getMemberId() {
        return memberId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
