package roomescape.waiting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.domain.WaitingWithRank;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class WaitingRepositoryTest {

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Test
    void 예약_대기_신청이_중복되었으면_true를_반환한다() {
        // given
        Theme theme = createTheme();
        Time time = createTime();
        LocalDate date = LocalDate.of(2025, 3, 30);

        Member member = createMember("멤버", "member@email.com");
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
        Theme theme = createTheme();
        Time time = createTime();
        LocalDate date = LocalDate.of(2025, 3, 30);

        Member member1 = createMember("멤버1", "member1@email.com");
        Waiting waiting1 = new Waiting(member1.getId(), member1.getName(), date, time, theme);
        waitingRepository.save(waiting1);

        Member member2 = createMember("멤버2", "member2@email.com");
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
        Theme theme = createTheme();
        Time time = createTime();
        LocalDate date = LocalDate.of(2025, 3, 30);

        Member member1 = createMember("멤버1", "member1@email.com");
        Waiting waiting1 = new Waiting(member1.getId(), member1.getName(), date, time, theme);
        waitingRepository.save(waiting1);

        Member member2 = createMember("멤버2", "member2@email.com");
        Waiting waiting2 = new Waiting(member2.getId(), member2.getName(), date, time, theme);
        waitingRepository.save(waiting2);
        // when
        Optional<Waiting> expectedFirstWaiting = waitingRepository.findFirstWaitingByDateAndTimeAndTheme(date, time, theme);
        // then
        assertThat(expectedFirstWaiting).hasValueSatisfying(
                waiting -> waiting.getMemberId().equals(member1.getId())
        );
    }

    private Theme createTheme() {
        Theme theme = new Theme("커스텀테마1", "커스텀테마 입니다.");
        return themeRepository.save(theme);
    }

    private Time createTime() {
        Time time = new Time(LocalTime.of(22, 0));
        return timeRepository.save(time);
    }

    private Member createMember(String name, String email) {
        Member member = new Member(name, email, "password", Role.USER);
        return memberRepository.save(member);
    }
}
