package roomescape.waiting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.Time;
import roomescape.waiting.dto.WaitingWithRank;
import roomescape.waiting.entity.Waiting;

import java.time.LocalDate;
import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT new roomescape.waiting.dto.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id)) " +
            "FROM waiting w " +
            "JOIN FETCH w.theme " +
            "JOIN FETCH w.time " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    long countByDateAndTimeAndTheme(LocalDate date, Time time, Theme theme);
}
