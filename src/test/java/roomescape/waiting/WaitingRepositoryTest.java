package roomescape.waiting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@DataJpaTest
class WaitingRepositoryTest {
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WaitingRepository waitingRepository;

    private Member admin;
    private Member brown;
    private Time time;
    private Theme theme;

    @BeforeEach
    void setUp() {
        admin = new Member("어드민", "admin@email.com", "password", "ADMIN");
        brown = new Member("브라운", "brown@email.com", "password", "USER");
        time = new Time("10:00");
        theme = new Theme("테마1", "테마1입니다.");

        memberRepository.save(admin);
        memberRepository.save(brown);
        timeRepository.save(time);
        themeRepository.save(theme);
    }

    @Test
    void 순위를_포함한_예약_대기_조회_성공() {
        // given
        Waiting adminWaiting = new Waiting("어드민", "2024-03-01", time, theme, admin);
        Waiting brownWaiting = new Waiting("브라운", "2024-03-01", time, theme, brown);
        waitingRepository.save(adminWaiting);
        waitingRepository.save(brownWaiting);

        // when
        List<WaitingWithRank> adminWaitings = waitingRepository.findAllWithRankByMemberId(admin.getId());
        List<WaitingWithRank> brownWaitings = waitingRepository.findAllWithRankByMemberId(brown.getId());

        // then
        assertThat(adminWaitings.size()).isEqualTo(1);
        assertThat(adminWaitings.get(0).rank).isEqualTo(0);
        assertThat(brownWaitings.size()).isEqualTo(1);
        assertThat(brownWaitings.get(0).rank).isEqualTo(1);
    }
}
