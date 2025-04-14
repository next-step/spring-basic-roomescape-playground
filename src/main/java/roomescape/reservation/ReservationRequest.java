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
        return new Reservation(member, member.getName(), date, reservationTime, theme);
    }
}
