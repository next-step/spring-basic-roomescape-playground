package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("""
        SELECT new roomescape.waiting.WaitingWithRank(
            w.id,
            theme.name,
            w.date,
            time.value,
            (SELECT COUNT(w2)
             FROM Waiting w2
             WHERE w2.theme = w.theme
               AND w2.date = w.date
               AND w2.time = w.time
               AND w2.waitingOrder < w.waitingOrder)
        )
        FROM Waiting w
        JOIN w.theme theme
        JOIN w.time time
        WHERE w.memberId = :memberId
        """)
    List<WaitingWithRank> findWaitingsWithRankByMemberId(
            @Param("memberId") Long memberId
    );

    boolean existsByMemberIdAndDateAndTimeIdAndThemeId(
            Long memberId,
            String date,
            Long timeId,
            Long themeId
    );

    Optional<Waiting> findByIdAndMemberId(
            Long id,
            Long memberId
    );

    @Query("""
        SELECT COALESCE(MAX(w.waitingOrder), 0)
        FROM Waiting w
        WHERE w.theme.id = :themeId
          AND w.date = :date
          AND w.time.id = :timeId
        """)
    Long findMaxWaitingOrder(
            @Param("themeId") Long themeId,
            @Param("date") String date,
            @Param("timeId") Long timeId
    );
}
