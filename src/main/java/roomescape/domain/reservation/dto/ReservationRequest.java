package roomescape.domain.reservation.dto;

import roomescape.domain.member.entity.Member;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

public class ReservationRequest {

    private String name;
    private String date;
    private Long theme;
    private Long time;

    public ReservationRequest() {
    }

    public ReservationRequest(final String name, final String date, final Long theme,
                              final Long time) {
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
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

    public Reservation toEntity(final Time time, final Theme theme, final Member member) {
        return new Reservation(name, date, time, theme, member);
    }
}
