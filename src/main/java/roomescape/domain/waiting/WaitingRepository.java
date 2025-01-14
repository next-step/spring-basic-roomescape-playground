package roomescape.domain.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findByDateAndTimeIdAndThemeId(String date, Long timeId, Long themeId);

    @Query("SELECT new roomescape.domain.waiting.WaitingWithRank(" +
            "    w, " +
            "    (SELECT CAST(COUNT(w2) AS long) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingWithRankByMemberId(@Param("memberId") Long memberId);
}
