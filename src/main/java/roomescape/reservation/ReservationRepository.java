package roomescape.reservation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"theme", "time"})
    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    @EntityGraph(attributePaths = {"theme", "time"})
    List<Reservation> findByMemberId(Long memberId);

    boolean existsByThemeId(Long themeId);

    boolean existsByTimeId(Long id);

    boolean existsByThemeIdAndDateAndTimeId(Long themeId, LocalDate date, Long timeId);

    boolean existsByMemberIdAndThemeIdAndDateAndTimeId(Long memberId, Long themeId, LocalDate date, Long timeId);
}
