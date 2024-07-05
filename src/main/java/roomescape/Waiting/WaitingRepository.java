package roomescape.Waiting;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WaitingRepository extends CrudRepository<Waiting, Long> {

    @Query("SELECT new roomescape.Waiting.WaitingWithRank(w, " +
            "(SELECT CAST(COUNT(w2) AS long) FROM Waiting w2 WHERE w2.theme = w.theme AND w2.date = w.date AND w2.time = w.time AND w2.id < w.id)) " +
            "FROM Waiting w WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);

    @Query("SELECT COUNT(w) FROM Waiting w WHERE w.theme.id = :themeId AND w.date = :date AND w.time.id = :timeId AND w.id < :waitingId ")
    Integer countWaitingNumber(Long waitingId, Long themeId, Long timeId, String date);

    boolean existsByThemeIdAndTimeIdAndDateAndMemberId(Long themeId, Long timeId, String date, Long memberId);
}