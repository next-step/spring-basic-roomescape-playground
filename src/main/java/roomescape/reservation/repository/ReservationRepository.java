package roomescape.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import roomescape.reservation.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(String date, Long theme_id);

    List<Reservation> findByMemberId(Long memberId);
}
