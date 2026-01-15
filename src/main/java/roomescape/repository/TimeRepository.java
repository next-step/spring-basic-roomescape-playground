package roomescape.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.model.Time;

public interface TimeRepository extends JpaRepository<Time, Long> { }
