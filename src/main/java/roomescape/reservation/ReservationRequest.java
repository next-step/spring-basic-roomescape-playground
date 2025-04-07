package roomescape.reservation;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.reservationTime.ReservationTime;

public record ReservationRequest(@NotNull String name, @NotNull LocalDate date, long theme,
                                 long time) {

    public Reservation toReservationWithMember(Theme theme, ReservationTime reservationTime,
                                               Member member) {
        return new Reservation(member, name, date, reservationTime, theme);
    }

    public ReservationRequest update(String name) {
        return new ReservationRequest(name, this.date, this.theme, this.time);
    }
}
