package roomescape.fixture;

import roomescape.member.entity.Member;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.Time;
import roomescape.waiting.entity.Waiting;

import java.time.LocalDate;

@SuppressWarnings("NonAsciiCharacters")
public class WaitingFixture {

    public static Waiting 내일_대기_생성(
            Member member,
            Time time,
            Theme theme
    ) {
        return new Waiting(LocalDate.now().plusDays(1), member, time, theme);
    }

    public static Waiting 지정_날짜_대기_생성(
            LocalDate date,
            Member member,
            Time time,
            Theme theme
    ) {
        return new Waiting(date, member, time, theme);

    }
}
