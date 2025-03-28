package roomescape.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.auth.dto.LoginMember;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.response.MyReservationResponse;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(LocalDate date, long themeId);

    List<Reservation> findByMemberId(Long id);
}
