package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMemberId(Long id);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    boolean existsByDateAndTimeAndTheme(String date, Time time, Theme theme);
}