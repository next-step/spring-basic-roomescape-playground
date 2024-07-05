package roomescape.time;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<Time, Long> {

	default Time getById(Long id) {
		return findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid time id"));
	}
}
