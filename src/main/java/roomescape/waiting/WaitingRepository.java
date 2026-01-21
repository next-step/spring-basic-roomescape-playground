package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT w FROM Waiting w " +
            "JOIN FETCH w.member " +
            "WHERE w.id = :id")
    Optional<Waiting> findByIdWithMember(Long id);

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
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);

    @Query("SELECT w FROM Waiting w " +
            "WHERE w.date = :date " +
            "AND w.time.id = :timeId " +
            "AND w.theme.id = :themeId")
    List<Waiting> findByDateAndTimeIdAndThemeId(String date, Long timeId, Long themeId);

    @Query("SELECT COUNT(w) FROM Waiting w " +
            "WHERE w.date = :date " +
            "AND w.time.id = :timeId " +
            "AND w.theme.id = :themeId")
    long countByDateAndTimeIdAndThemeId(String date, Long timeId, Long themeId);

    boolean existsByMemberIdAndDateAndTimeIdAndThemeId(Long memberId, String date, Long timeId, Long themeId);
}
