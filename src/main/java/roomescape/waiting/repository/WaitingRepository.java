package roomescape.waiting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.waiting.WaitingWithRank;
import roomescape.waiting.domain.Waiting;

import java.time.LocalDate;
import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    boolean existsByMember_IdAndDateAndTheme_IdAndTime_Id(
            Long memberId,
            LocalDate date,
            Long themeId,
            Long timeId
    );

    @Query("""
            SELECT new roomescape.waiting.WaitingWithRank(
                w.id,
                w.theme.name,
                w.date,
                w.time.value,
                (SELECT COUNT(w2)
                 FROM Waiting w2
                 WHERE w2.theme = w.theme
                   AND w2.date = w.date
                   AND w2.time = w.time
                   AND w2.id < w.id)
            )
            FROM Waiting w
            WHERE w.member.id = :memberId
            ORDER BY w.id
            """)
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    long countByDateAndTheme_IdAndTime_Id(LocalDate date, Long themeId, Long timeId);
}
