package roomescape.time;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeRepository extends JpaRepository<Time, Long> {
    Optional<Time> findByValue(String value);
}
