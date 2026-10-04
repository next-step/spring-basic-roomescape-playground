package roomescape.waiting;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WaitingRepository extends CrudRepository<Waiting, Long> {
    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    CAST((SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id) AS Long)) " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);
    boolean existsByDateAndTimeIdAndThemeIdAndMemberId(String date, Long timeId, Long themeId, Long memberId);



}
