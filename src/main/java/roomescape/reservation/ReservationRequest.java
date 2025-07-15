package roomescape.reservation;

import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long theme, Long time) {

    public ReservationRequest {
        if (date == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "날짜는 필수입니다.");
        }
        if (theme == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "테마는 필수입니다.");
        }
        if (time == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "시간은 필수입니다.");
        }
    }

    public ReservationRequest withDefaultName(String defaultName) {
        return new ReservationRequest(defaultName, date, theme, time);
    }

    public Reservation toEntity(String name, Time time, Theme theme, Member member) {
        return new Reservation(name, this.date, time, theme, member);
    }
}
