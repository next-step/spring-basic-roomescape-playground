package roomescape.domain.reservation;

public class ReservationRequest {
    private Long memberId;
    private String name;
    private String date;
    private String theme;
    private String time;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public String getTheme() {
        return theme;
    }

    public String getTime() {
        return time;
    }
}
