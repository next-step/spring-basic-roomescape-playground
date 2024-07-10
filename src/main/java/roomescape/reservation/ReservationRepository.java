package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation save(ReservationRequest request);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByName(String name);
}
