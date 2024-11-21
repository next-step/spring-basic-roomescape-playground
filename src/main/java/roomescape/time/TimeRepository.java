package roomescape.time;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TimeRepository extends JpaRepository<Time, Long> {
}
