package roomescape.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import roomescape.theme.Theme;
import roomescape.time.Time;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByName(String name);

	List<Reservation> findByDateAndThemeId(String date, Long themeId);

	boolean existsByDateAndTimeAndTheme(String date, Time time, Theme theme);
}
