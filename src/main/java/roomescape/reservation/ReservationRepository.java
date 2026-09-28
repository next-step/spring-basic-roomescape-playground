package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationByDateAndTheme(String date, Long themeId);
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
