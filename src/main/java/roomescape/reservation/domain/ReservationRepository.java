package roomescape.reservation.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    @EntityGraph(attributePaths = {"member", "theme", "time"})
    List<Reservation> findMyReservationsByName(String name);
}
