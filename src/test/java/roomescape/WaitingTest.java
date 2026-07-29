package roomescape;

import org.junit.jupiter.api.Test;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;
import roomescape.waiting.Waiting;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitingTest {

    @Test
    void 동일한_조건의_대기_신청인지_확인_테스트(){

        Time time = new Time();
        Theme theme = new Theme();
        Member member =new Member(1L,"MEMBER");
        Waiting waiting = new Waiting("2024-03-01", time, theme, member);

        boolean isSame = waiting.isSameWaiting("2024-03-01", time, theme, member);

        assertThat(isSame).isTrue();
    }



}
