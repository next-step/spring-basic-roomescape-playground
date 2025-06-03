package roomescape.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByDateAndThemeId(String date, Long themeId);

    List<Reservation> findAllByMemberId(Long memberId);

}
