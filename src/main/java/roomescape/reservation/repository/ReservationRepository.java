package roomescape.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.reservation.domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
    List<Reservation> findAllByMemberId(Long memberId);
    Optional<Reservation> findByDateAndThemeIdAndTimeId(String date, Long themeId, Long timeId);
}
