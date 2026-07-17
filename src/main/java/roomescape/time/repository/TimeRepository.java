package roomescape.time.repository;

import roomescape.time.entity.Time;

import java.util.List;
import java.util.Optional;

public interface TimeRepository {

    List<Time> findAll();

    Optional<Time> findById(Long id);

    Time save(Time time);

    void deleteById(Long id);
}
