package roomescape.repository;

import org.junit.jupiter.api.Test;
import roomescape.member.entity.Member;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.Time;
import roomescape.waiting.dto.WaitingWithRank;
import roomescape.waiting.entity.Waiting;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.MemberFixture.관리자_관리자1_생성;
import static roomescape.fixture.MemberFixture.멤버_멤버1_생성;
import static roomescape.fixture.MemberFixture.멤버_멤버2_생성;
import static roomescape.fixture.ThemeFixture.테마_테마1_생성;
import static roomescape.fixture.TimeFixture.시간_시간1_생성;
import static roomescape.fixture.WaitingFixture.내일_대기_생성;

@SuppressWarnings("NonAsciiCharacters")
public class WaitingRepositoryTest extends RepositoryTest {

    @Test
    void 먼저_등록된_대기_수를_기준으로_회원의_대기_순위를_조회한다() {
        // given
        Member admin1 = 관리자_관리자1_생성();
        Member member1 = 멤버_멤버1_생성();
        Member member2 = 멤버_멤버2_생성();
        복수_멤버_저장(admin1, member1, member2);

        Time time = 시간_시간1_생성();
        단일_시간_저장(time);

        Theme theme = 테마_테마1_생성();
        단일_테마_저장(theme);

        Waiting waiting1 = 내일_대기_생성(admin1, time, theme);
        Waiting waiting2 = 내일_대기_생성(member1, time, theme);
        Waiting waiting3 = 내일_대기_생성(member2, time, theme);
        복수_대기_저장(waiting1, waiting2, waiting3);

        // when
        WaitingWithRank waitingWithRank =
                waitingRepository.findWaitingsWithRankByMemberId(member2.getId())
                        .get(0);

        // then
        assertThat(waitingWithRank.rank())
                .isEqualTo(2L);

    }
}
