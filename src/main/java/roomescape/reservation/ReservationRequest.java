package roomescape.reservation;

import roomescape.member.LoginMember;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long theme, Long time) {

    public void validate() {
        if (this.name == null || this.date == null || this.theme == null || this.time == null) {
            throw new IllegalArgumentException("Invalid reservation request");
        }
    }
}
