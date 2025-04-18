package roomescape.time;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<Time, Long> {

    List<Time> findAllByDeletedFalse();

    default Time getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다."));
    }
}
