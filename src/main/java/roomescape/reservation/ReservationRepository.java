package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomescape.theme.Theme;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation save(ReservationRequest reservationRequest);

//    @Query("select r from Reservation r where r.date = :date and r.theme = :theme")
//    List<Reservation> findReservationsByDateAndTheme(String date, Theme theme);

    @Query("select r from Reservation r where r.date = :date and r.theme.id = :themeId")
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
