package roomescape.waiting;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Repository
public interface WaitingRepository extends CrudRepository<Waiting, Long> {
    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id <= w.id)) " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findAllWithRankByMemberId(Long memberId);

    boolean existsByDateAndTimeAndThemeAndMember(String date, Time time, Theme theme, Member member);
}
