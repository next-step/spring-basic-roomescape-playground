package roomescape.reservation;

import roomescape.theme.Theme;
import roomescape.time.Time;

public class ReservationRequest {
    private Long memberId;
    private String name;
    private String date;
    private Theme theme;
    private Time time;

    public Long getMemberId(){
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

    public Theme getTheme() {
        return theme;
    }

    public Time getTime() {
        return time;
    }
}
