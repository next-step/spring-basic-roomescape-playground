package roomescape.reservation.dto;

import roomescape.theme.Theme;
import roomescape.time.Time;

public class ReservationRequest {
    private Long member;
    private String name;
    private String date;
    private Long theme;
    private Long time;

    public void setName(String name) {
        this.name = name;
    }

    public void setMember(Long member) {
        this.member = member;
    }

    public Long getMember() {
        return member;
    }

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
}
