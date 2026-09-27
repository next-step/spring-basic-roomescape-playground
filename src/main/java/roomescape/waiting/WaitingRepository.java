package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "WHERE w.memberId = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    boolean existsByDateAndTheme_IdAndTime_IdAndMemberId(String date, Long themeId, Long timeId, Long memberId);

    // 예약 대기 순번을 구하기 위한 쿼리
    @Query("SELECT COUNT(w) FROM Waiting w " +
            "WHERE w.theme.id = :themeId AND w.date = :date AND w.time.id = :timeId AND w.id < :id")
    long countByDateAndTheme_IdAndTime_IdAndIdLessThan(
            @Param("date") String date, @Param("themeId") Long themeId,
            @Param("timeId") Long timeId, @Param("id") Long id);
}
