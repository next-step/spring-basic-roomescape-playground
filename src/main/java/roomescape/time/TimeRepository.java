package roomescape.time;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimeRepository extends JpaRepository<Time, Long> {

    List<Time> findByDeletedFalse();

    @Modifying
    @Query("UPDATE Time t SET t.deleted = true WHERE t.id = :id")
    void DeleteById(@Param("id") Long id);
}
