package roomescape.waiting;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    boolean existsByMemberIdAndDateAndTimeIdAndThemeId(Long memberId, String date, Long timeId, Long themeId);

    long countByDateAndTimeIdAndThemeIdAndIdLessThan(String date, Long timeId, Long themeId, Long id);

    @EntityGraph(attributePaths = {"time", "theme"})
    @Query("""
            select new roomescape.waiting.WaitingWithRank(w,
                (select count(w2) from Waiting w2
                 where w2.date = w.date
                   and w2.time.id = w.time.id
                   and w2.theme.id = w.theme.id
                   and w2.id < w.id))
            from Waiting w
            where w.member.id = :memberId
            order by w.id
            """)
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);
}
