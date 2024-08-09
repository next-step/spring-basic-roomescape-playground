package roomescape.waiting.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    @Query("select count(w) " +
            " from Waiting w " +
            " where w.theme = :theme " +
            " and w.time = :time " +
            " and w.date = :date" +
            " and w.id < :id")
    long countWaitingBy(@Param("theme") Theme theme,
                        @Param("time") Time time,
                        @Param("date") String date,
                        @Param("id") Long id);

    @EntityGraph(attributePaths = {"member", "theme", "time"})
    List<Waiting> findMyWaitingByName(String memberName);

    boolean existsByMemberNameAndId(String memberName, Long waitingId);
}
