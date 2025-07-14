package roomescape.waiting;

import roomescape.auth.LoginMember;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;

public record WaitingRequest(
        LocalDate date,
        Long theme,
        Long time
) {
    public WaitingRequest {
        if (date == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "날짜는 필수입니다");
        }
        if (theme == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "테마는 필수입니다");
        }
        if (time == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "테마는 필수입니다");
        }
    }

    public Waiting toEntity(String name, LocalDate date, Time time, Theme theme, Member member) {
        return new Waiting(name, date, time, theme, member);
    }
}
