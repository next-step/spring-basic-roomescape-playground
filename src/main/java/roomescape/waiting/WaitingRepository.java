package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {


    List<Waiting> findByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, String date );

    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    CAST((SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id) AS LONG)) " +
            "FROM Waiting w " +
            "WHERE w.memberId = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);

}

