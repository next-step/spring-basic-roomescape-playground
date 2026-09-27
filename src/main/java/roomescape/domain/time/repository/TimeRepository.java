package roomescape.domain.time.repository;

import org.springframework.data.repository.ListCrudRepository;
import roomescape.domain.time.entity.Time;

import java.time.LocalTime;

public interface TimeRepository extends ListCrudRepository<Time,Long> {
    boolean existsByTimeValue(LocalTime timeValue);
}
