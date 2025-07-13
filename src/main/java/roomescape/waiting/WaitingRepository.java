package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;
import roomescape.waiting.dto.WaitingWithRank;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    boolean existsByThemeAndDateAndTimeAndMember(Theme theme, LocalDate date, Time time, Member member);

    @Query("""
            SELECT new roomescape.waiting.dto.WaitingWithRank(
                w,
                (
                    SELECT CAST(COUNT(w2) AS long)
                    FROM Waiting w2
                    WHERE w2.theme = w.theme
                      AND w2.date = w.date
                      AND w2.time = w.time
                      AND w2.id < w.id
                )
            )
            FROM Waiting w
            JOIN FETCH w.theme
            JOIN FETCH w.time
            WHERE w.member.id = :memberId
            """)
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);
}
