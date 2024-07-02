package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomescape.waiting.dto.WaitingWithRank;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting,Long> {
    @Query("SELECT new roomescape.waiting.dto.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingWithRankByMemberId(Long memberId);
    List<Waiting> findByThemeIdAndDateAndTimeId(Long themeId, String date, Long timeId);
}
