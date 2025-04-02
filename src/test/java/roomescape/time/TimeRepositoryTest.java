package roomescape.time;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TimeRepositoryTest {

	@Autowired
	private TimeRepository timeRepository;

	@DisplayName("findAllByDeletedFalse : 새로 저장한 Time 모두 조회 시 포함한다.")
	@Test
	void given_new_timeEntity_when_findAll_then_contain_result() {
		//given
		Time time = Time.ofDeletedFalse("10:00");
		timeRepository.save(time);
		// when
		List<Time> times = timeRepository.findAllByDeletedFalse();
		// then
		assertThat(times).contains(time);
	}

	@DisplayName("findAllByDeletedFalse : 삭제된 Time은 조회되지 않는다.")
	@Test
	void given_deleted_timeEntity_when_findAll_then_not_contain_result() {
		//given
		Time time = Time.ofDeletedFalse("10:00");
		timeRepository.save(time);
		time.markAsDeleted();
		// when
		List<Time> times = timeRepository.findAllByDeletedFalse();
		// then
		assertThat(times).doesNotContain(time);
	}
}
