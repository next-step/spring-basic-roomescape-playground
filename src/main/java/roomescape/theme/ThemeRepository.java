package roomescape.theme;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.reservation.Reservation;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

}
