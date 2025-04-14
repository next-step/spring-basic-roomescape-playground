package roomescape.waiting;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("""
            SELECT COUNT(w)
              FROM Waiting w
             WHERE w.theme = :theme
              AND w.date = :date
              AND w.time = :time
              AND w.id <= :id
            """)
    Long countByConditions(
            @Param("theme") Theme theme,
            @Param("date") String date,
            @Param("time") Time time,
            @Param("id") Long id
    );

    @Query("""
            SELECT new roomescape.waiting.WaitingWithRank(
                w,
                (
                    SELECT COUNT(w2)
                     FROM Waiting w2
                     WHERE w2.theme = w.theme
                       AND w2.date = w.date
                       AND w2.time = w.time
                       AND w2.id < w.id
                       )
               )
            FROM Waiting w
            WHERE w.member.id = :memberId
            """)
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);

    boolean existsByThemeAndDateAndTimeAndMember(Theme theme, String date, Time time, Member member);

    Optional<Waiting> findFirstByThemeAndTimeAndDateOrderById(Theme theme, Time time, String date);
}
