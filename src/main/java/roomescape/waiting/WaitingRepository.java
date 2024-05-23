package roomescape.waiting;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WaitingRepository extends CrudRepository<Waiting, Long> {

    @Query("SELECT new roomescape.waiting.WaitingWithRank(w, " +
            "(SELECT COUNT(w2) FROM Waiting w2 WHERE w2.theme = w.theme AND w2.date = w.date AND w2.time = w.time AND w2.id < w.id)) FROM Waiting w WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);


    @Query("SELECT COUNT(w) FROM Waiting w WHERE w.theme.id = :themeId AND w.date = :date AND w.time.id = :timeId AND w.id < :waitingId ")
    int countWaitingNumber(Long waitingId, Long themeId, Long timeId, String date);

    boolean existsByThemeIdAndTimeIdAndDateAndMemberId(Long themeId, Long timeId, String date, Long memberId);
}
