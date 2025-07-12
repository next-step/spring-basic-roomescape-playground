package roomescape.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"time", "theme"})
    @Query("SELECT r FROM Reservation r")
    List<Reservation> findAllWithFetch();

    @EntityGraph(attributePaths = {"time", "theme"})
    List<Reservation> findByMemberId(Long memberId);

    @EntityGraph(attributePaths = {"time", "theme"})
    @Query("SELECT r FROM Reservation r WHERE r.date = :date AND r.theme.id = :themeId")
    List<Reservation> findByDateAndThemeId(
            @Param("date") String date,
            @Param("themeId") Long themeId
    );
}
