package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndTheme_Id(LocalDate date, long themeId);

    @Query("select r from Reservation r join fetch r.reservationTime")
    List<Reservation> findAllWithReservationTime();
}
