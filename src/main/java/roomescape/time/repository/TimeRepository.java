package roomescape.time.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.time.entity.Time;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {
}
