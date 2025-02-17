package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAll();

    void deleteById(Long id);

    @Query("SELECT r FROM Reservation r WHERE r.date = :date AND r.theme.id = :themeId")
    List<Reservation> findReservationsByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
