package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;
import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    boolean existsByMemberAndDateAndTimeAndTheme(Member member, LocalDate date, Time time, Theme theme);

    Long countByDateAndTimeAndThemeAndIdLessThan(LocalDate date, Time time, Theme theme, Long id);

    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);
}
