package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.theme.Theme;

import java.util.List;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndTheme(String date, Theme theme);
}
