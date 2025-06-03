package roomescape.time;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimeRepository extends JpaRepository<Time, Long> {

    List<Time> findAllByDeletedFalse();

    @Modifying
    @Query("UPDATE Time e SET e.deleted = true WHERE e.id = :id")
    void softDeleteById(@Param("id") Long id);

}
