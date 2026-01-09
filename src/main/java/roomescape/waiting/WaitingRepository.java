package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT w FROM Waiting w " +
            "JOIN FETCH w.member " +
            "WHERE w.id = :id")
    Optional<Waiting> findByIdWithMember(@Param("id") Long id);

    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "JOIN FETCH w.time " +
            "JOIN FETCH w.theme " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT w FROM Waiting w " +
            "WHERE w.date = :date " +
            "AND w.time.id = :timeId " +
            "AND w.theme.id = :themeId")
    List<Waiting> findByDateAndTimeIdAndThemeId(
            @Param("date") String date,
            @Param("timeId") Long timeId,
            @Param("themeId") Long themeId
    );

    @Query("SELECT COUNT(w) FROM Waiting w " +
            "WHERE w.date = :date " +
            "AND w.time.id = :timeId " +
            "AND w.theme.id = :themeId")
    long countByDateAndTimeIdAndThemeId(
            @Param("date") String date,
            @Param("timeId") Long timeId,
            @Param("themeId") Long themeId
    );
}
