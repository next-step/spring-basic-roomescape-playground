package roomescape.time.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;
import roomescape.time.domain.Time;
import roomescape.time.exception.TimeNotFoundException;

public interface TimeRepository extends JpaRepository<Time, Long> {

    Optional<Time> findById(Long timeId);

    default Time getById(Long timeId) {
        return findById(timeId)
            .orElseThrow(TimeNotFoundException::withMessage);
    }

}
