package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> { // Long -> (pk)의 자료형
    List<Reservation> findReservationByDateAndThemeId(String date, Long themeId);
}
