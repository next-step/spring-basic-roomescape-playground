package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.reservation.DTO.ReservationRequest;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    public Reservation save(ReservationRequest newReservation);

    public List<Reservation> findByDateAndThemeId(String date, Long themeId);
}