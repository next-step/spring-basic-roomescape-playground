package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.Waiting;
import roomescape.reservation.dto.WaitingWithRank;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT w FROM Waiting w " +
        "WHERE w.date = :date " +
        "AND w.theme = :theme " +
        "AND w.time = :time " +
        "AND w.member = :member")
    Optional<Waiting> findExistWaiting(
        @Param("date") String date,
        @Param("theme") Theme theme,
        @Param("time") Time time,
        @Param("member") Member member
    );

    @Query("SELECT new roomescape.reservation.dto.WaitingWithRank(" +
        "    w, " +
        "    (SELECT COUNT(w2) " +
        "     FROM Waiting w2 " +
        "     WHERE w2.theme = w.theme " +
        "       AND w2.date = w.date " +
        "       AND w2.time = w.time " +
        "       AND w2.id < w.id)) " +
        "FROM Waiting w " +
        "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(w) + 1 FROM Waiting w " +
        "WHERE w.theme = :theme " +
        "AND w.date = :date " +
        "AND w.time = :time")
    Integer findWaitingRank(@Param("theme") Theme theme,
        @Param("date") String date,
        @Param("time") Time time);
}
