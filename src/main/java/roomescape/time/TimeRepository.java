package roomescape.time;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TimeRepository extends JpaRepository<Time, Long> {
    List<Time> findAllByDeletedFalse();

    @Transactional
    @Modifying
    @Query("update Time time set time.deleted = true where time.id = :id")
    void softDeleteById(Long id);
}
