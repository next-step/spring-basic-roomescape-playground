package roomescape.time.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.time.domain.Time;

import java.util.List;
import java.util.Optional;

public interface TimeRepository extends JpaRepository<Time, Long> {
    List<Time> findAllByDeletedFalse();
    Optional<Time> findByIdAndDeletedFalse(Long id);
    Optional<Time> findByValue(String value);
}
