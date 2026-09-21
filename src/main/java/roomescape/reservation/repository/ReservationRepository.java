package roomescape.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.reservation.domain.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
