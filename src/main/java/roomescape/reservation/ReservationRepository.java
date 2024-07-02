package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
    List<Reservation> findByName(String name);

    Optional<Reservation> findByDateAndThemeIdAndTimeId(ReservationRequest reservationRequest, Long themeId, Long timeId);

    List<Reservation> findByMemberId(Long memberId);
}
