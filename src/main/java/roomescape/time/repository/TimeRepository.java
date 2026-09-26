package roomescape.time.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.time.domain.Time;

import java.time.LocalDate;
import java.util.List;

public interface TimeRepository extends JpaRepository<Time, Long> {
    @Query("""
            SELECT new roomescape.time.repository.AvailableTimeProjection(
                t.id,
                t.value,
                CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            )
            FROM Time t
            LEFT JOIN Reservation r
                ON r.time = t
                AND r.date = :date
                AND r.theme.id = :themeId
            GROUP BY t.id, t.value
            ORDER BY t.id
            """)
    List<AvailableTimeProjection> findAvailableTimes(@Param("date") LocalDate date,
                                                     @Param("themeId") Long themeId);
}
