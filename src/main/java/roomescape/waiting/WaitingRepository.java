package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    boolean existsByMemberIdAndThemeIdAndDateAndTimeId(Long memberId, Long themeId, LocalDate date, Long timeId);
    boolean existsByTimeId(Long id);
    boolean existsByThemeId(Long id);

    @Query("""
                SELECT COUNT(w) + 1 FROM Waiting w
                WHERE w.theme = :theme
                  AND w.date = :date
                  AND w.time = :time
                  AND w.id < :id
                  AND w.time.deleted = false
                  AND w.theme.deleted = false
            """)
    Long getWaitingRank(
            @Param("theme") Theme theme,
            @Param("date") LocalDate date,
            @Param("time") Time time,
            @Param("id") Long id
    );

    @Query("""
            SELECT new roomescape.waiting.WaitingWithRank(
                w,
                (SELECT COUNT(w2) + 1
                 FROM Waiting w2
                 WHERE w2.theme = w.theme
                   AND w2.date = w.date
                   AND w2.time = w.time
                   AND w2.id < w.id
                   AND w2.theme.deleted = false
                   AND w2.time.deleted = false)
            )
            FROM Waiting w
            WHERE w.member.id = :memberId
            """)
    List<WaitingWithRank> findWaitingWithRankByMemberId(Long memberId);
}
