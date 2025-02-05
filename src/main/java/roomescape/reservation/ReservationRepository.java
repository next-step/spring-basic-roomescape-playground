package roomescape.reservation;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findAll();

    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
