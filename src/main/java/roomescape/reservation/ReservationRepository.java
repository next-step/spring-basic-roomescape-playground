package roomescape.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time")
    List<Reservation> findAll();

    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
