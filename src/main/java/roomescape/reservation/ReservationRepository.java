package roomescape.reservation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByDateAndThemeIdAndTimeId(String date, Long themeId, Long timeId);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByName(String name);

    List<Reservation> findByMemberId(Long memberId);
}
