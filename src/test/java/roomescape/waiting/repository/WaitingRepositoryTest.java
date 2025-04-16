package roomescape.waiting.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import roomescape.fixture.FixtureConfig;
import roomescape.fixture.FixtureGenerator;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.domain.Waiting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(FixtureConfig.class)
class WaitingRepositoryTest {

    @Autowired
    private FixtureGenerator fixtureGenerator;

    @Autowired
    private WaitingRepository waitingRepository;

    private Theme theme;
    private Time time;

    @BeforeEach
    void setUp() {
        theme = fixtureGenerator.createTheme();
        time = fixtureGenerator.createTime();
    }

    @Test
    void 예약_대기_신청이_중복되었으면_true를_반환한다() {
        // given
        LocalDate date = LocalDate.of(2025, 3, 30);

        Member member = fixtureGenerator.createMember("멤버", "member@email.com");
        Waiting waiting = new Waiting(member.getId(), member.getName(), date, time, theme);
        waitingRepository.save(waiting);
        // when
        boolean isDuplicateWaiting = waitingRepository.existsByMemberIdAndDateAndTimeAndTheme(member.getId(), LocalDate.of(2025, 3, 30), time, theme);
        // then
        assertThat(isDuplicateWaiting).isTrue();
    }

    @Test
    void 멤버의_예약_대기_순서를_조회한다() {
        // given
        LocalDate date = LocalDate.of(2025, 3, 30);

        Member member1 = fixtureGenerator.createMember("멤버1", "member1@email.com");
        Waiting waiting1 = new Waiting(member1.getId(), member1.getName(), date, time, theme);
        waitingRepository.save(waiting1);

        Member member2 = fixtureGenerator.createMember("멤버2", "member2@email.com");
        Waiting waiting2 = new Waiting(member2.getId(), member2.getName(), date, time, theme);
        waitingRepository.save(waiting2);
        // when
        Long rankOfMember2 = waitingRepository.findWaitingNumberByMemberId(member2.getId());
        // then
        assertThat(rankOfMember2).isEqualTo(2);
    }

    @Test
    void 특정_날짜_시간_테마에_해당하는_첫번째_대기를_조회한다() {
        // given
        LocalDate date = LocalDate.of(2025, 3, 30);

        Member member1 = fixtureGenerator.createMember("멤버1", "member1@email.com");
        Waiting waiting1 = new Waiting(member1.getId(), member1.getName(), date, time, theme);
        waitingRepository.save(waiting1);

        Member member2 = fixtureGenerator.createMember("멤버2", "member2@email.com");
        Waiting waiting2 = new Waiting(member2.getId(), member2.getName(), date, time, theme);
        waitingRepository.save(waiting2);
        // when
        Optional<Waiting> expectedFirstWaiting = waitingRepository.findFirstWaitingByDateAndTimeAndTheme(date, time, theme);
        // then
        assertThat(expectedFirstWaiting).hasValueSatisfying(
                waiting -> waiting.getMemberId().equals(member1.getId())
        );
    }
}
