package roomescape.time;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepository extends CrudRepository<Time, Long> {

    default Time findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException(String.format("Time Not found: %d", id)));
    }

    List<Time> findAll();
}
