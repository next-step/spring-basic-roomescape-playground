package roomescape.waiting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import roomescape.member.model.Member;
import roomescape.theme.model.Theme;
import roomescape.waiting.model.Waiting;
import roomescape.waiting.model.WaitingWithRank;

public interface WaitingRepository extends Repository<Waiting, Long> {

    Waiting save(Waiting waiting);

    List<Waiting> findAll();

    List<Waiting> findAllByMember(Member member);

    void deleteById(Long id);

    List<Waiting> findByDateAndTheme(String date, Theme theme);

    @Query("""
        SELECT new roomescape.waiting.model.WaitingWithRank(
                    w,
                    (SELECT COUNT(w2) + 1
                     FROM Waiting w2
                     WHERE w2.theme = w.theme
                       AND w2.date = w.date
                       AND w2.time = w.time
                       AND w2.id < w.id
                    )
                )
        FROM Waiting w
        WHERE w.member.id = :memberId
        """)
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT new roomescape.waiting.model.WaitingWithRank(
                    w,
                    (SELECT COUNT(w2) + 1
                     FROM Waiting w2
                     WHERE w2.theme = w.theme
                       AND w2.date = w.date
                       AND w2.time = w.time
                       AND w2.id < w.id
                    )
                )
        FROM Waiting w
        WHERE w.id = :id
        """)
    WaitingWithRank getWaitingWithRankById(@Param("id") Long id);
}
