package roomescape.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time")
    List<Reservation> findAllWithThemeAndTime();

    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
