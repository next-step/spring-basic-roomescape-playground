package roomescape.time;

import java.util.NoSuchElementException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {
    default Time getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new NoSuchElementException("Time not found"));
    }
}
