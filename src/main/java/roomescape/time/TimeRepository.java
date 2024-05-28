package roomescape.time;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface TimeRepository extends CrudRepository<Time, Long> {
    List<Time> findAll();
}
