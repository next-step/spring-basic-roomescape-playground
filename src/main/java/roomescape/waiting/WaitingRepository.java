package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.themeId = w.themeId " +
            "       AND w2.date = w.date " +
            "       AND w2.timeId = w.timeId " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "WHERE w.memberId = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);
}
