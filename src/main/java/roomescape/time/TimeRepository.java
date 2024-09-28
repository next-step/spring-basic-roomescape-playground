package roomescape.time;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface TimeRepository extends Repository<Time, Long> {

    List<Time> findAll();

    Optional<Time> findById(Long id);

    default Time getById(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Time not found"));
    }

    Time save(Time time);

    void deleteById(Long id);
}
