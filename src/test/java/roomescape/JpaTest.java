package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.theme.Theme;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

@DataJpaTest
public class JpaTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    @Test
    void 사단계() {
        Time time = new Time("10:00");
        entityManager.persist(time);
        entityManager.flush();

        Time persistTime = timeRepository.findById(time.getId()).orElse(null);

        assertThat(persistTime.getValue()).isEqualTo(time.getValue());
    }

    @Test
    void 대기_순위_조회() {
        Time time = new Time("10:00");
        Theme theme = new Theme("테마", "테마입니다.");
        entityManager.persist(time);
        entityManager.persist(theme);

        Waiting firstWaiting = new Waiting(1L, "2024-03-01", time, theme);
        Waiting secondWaiting = new Waiting(2L, "2024-03-01", time, theme);
        entityManager.persist(firstWaiting);
        entityManager.persist(secondWaiting);
        entityManager.flush();

        List<WaitingWithRank> firstMemberWaitings = waitingRepository.findWaitingsWithRankByMemberId(1L);
        List<WaitingWithRank> secondMemberWaitings = waitingRepository.findWaitingsWithRankByMemberId(2L);

        // rank = 나보다 먼저 생성된 같은 슬롯 대기 수
        assertThat(firstMemberWaitings).hasSize(1);
        assertThat(firstMemberWaitings.get(0).getRank()).isEqualTo(0L);
        assertThat(secondMemberWaitings).hasSize(1);
        assertThat(secondMemberWaitings.get(0).getRank()).isEqualTo(1L);
    }

    @Test
    void 중복_대기_존재_여부_조회() {
        Time time = new Time("10:00");
        Theme theme = new Theme("테마", "테마입니다.");
        entityManager.persist(time);
        entityManager.persist(theme);

        entityManager.persist(new Waiting(1L, "2024-03-01", time, theme));
        entityManager.flush();

        assertThat(waitingRepository.existsByMemberIdAndDateAndTimeAndTheme(1L, "2024-03-01", time, theme)).isTrue();
        assertThat(waitingRepository.existsByMemberIdAndDateAndTimeAndTheme(2L, "2024-03-01", time, theme)).isFalse();
        assertThat(waitingRepository.countByDateAndTimeAndTheme("2024-03-01", time, theme)).isEqualTo(1L);
    }
}
