package roomescape.domain.time.repository;

import roomescape.domain.time.entity.Time;

import java.util.List;

public interface TimeRepository {

    List<Time> findAll();

    Time save(Time time);

    void deleteById(Long timeId);
}
