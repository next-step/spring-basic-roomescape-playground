package roomescape.waiting.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import roomescape.member.model.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;
import roomescape.waiting.dto.WaitingWithRank;
import roomescape.waiting.model.Waiting;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

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
    List<WaitingWithRank> findWaitingWithRankByMemberId(@Param("memberId") Long memberId);

    Optional<Waiting> findById(Long id);

    default Waiting getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new NoSuchElementException("Waiting not found"));
    }

    Optional<Waiting> findByMemberAndThemeAndTimeAndDate(Member member, Theme theme, Time time, String date);

    List<Waiting> findByThemeAndTimeAndDate(Theme theme, Time time, String date);
}
