package roomescape.reservation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMemberId(Long memberId);
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
    Optional<Reservation> findByDateAndThemeIdAndTimeId(String date, Long themeId, Long timeId);
}
