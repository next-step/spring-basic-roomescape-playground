package roomescape.domain.time.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.domain.time.entity.Time;

public interface TimeRepository extends JpaRepository<Time, Long> {
}
