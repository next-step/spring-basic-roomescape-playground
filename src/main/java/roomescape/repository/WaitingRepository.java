package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.dto.WaitingWithRank;
import roomescape.model.Waiting;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    @Query("SELECT new roomescape.dto.WaitingWithRank(" +
           "   w, " +
           "   (SELECT COUNT(w2) + 1 " +
           "    FROM Waiting w2 " +
           "    WHERE w2.theme = w.theme " +
           "      AND w2.date = w.date " +
           "      AND w2.time = w.time " +
           "      AND w2.id < w.id)" +
           ") " +
           "FROM Waiting w " +
           "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END " +
           "FROM Waiting w " +
           "WHERE w.member.id = :memberId AND w.date = :date " +
           "AND w.time.id = :timeId AND w.theme.id = :themeId")
    boolean existsByMemberAndDateAndTimeAndTheme(@Param("memberId") Long memberId, @Param("date") LocalDate date, @Param("timeId") Long timeId, @Param("themeId") Long themeId);

    @Query("SELECT COUNT(w) FROM Waiting w WHERE w.date = :date AND w.time.id = :timeId AND w.theme.id = :themeId")
    Long countByDateAndTimeAndTheme(@Param("date") LocalDate date, @Param("timeId") Long timeId, @Param("themeId") Long themeId);
}
