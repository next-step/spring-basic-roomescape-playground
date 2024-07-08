package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.time.Time;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<MyReservationResponse> findByMemberId(Long memberId);

}