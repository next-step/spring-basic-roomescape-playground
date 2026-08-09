package roomescape.repository;

import org.junit.jupiter.api.Test;
import roomescape.time.entity.Time;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.TimeFixture.시간_시간1_생성;
import static roomescape.fixture.TimeFixture.시간_시간2_생성;
import static roomescape.fixture.TimeFixture.시간_시간3_생성;

@SuppressWarnings("NonAsciiCharacters")
public class TimeRepositoryTest extends RepositoryTest {

    @Test
    void 삭제되지_않은_시간만_조회한다() {
        // given
        final Time time1 = 시간_시간1_생성();
        final Time time2 = 시간_시간2_생성();
        final Time time3 = 시간_시간3_생성();
        복수_시간_저장(time1, time2, time3);

        time1.markDeleted();

        // when
        List<Time> nonDeletedTimes = timeRepository.findAllByDeletedAtNull();

        // then
        assertThat(nonDeletedTimes)
                .extracting(Time::getId)
                .containsExactlyInAnyOrder(
                        time2.getId(),
                        time3.getId()
                );
    }
}
