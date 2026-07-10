package roomescape.reservation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndTheme_IdAndIsWaitingFalse(String date, Long themeId);

    List<Reservation> findByDateAndTheme_IdAndIsWaitingTrueOrderByTime_TimeValueAscCreatedAtAsc(String date, Long themeId);

    Optional<Reservation> findFirstByDateAndTheme_IdAndIsWaitingTrueOrderByTime_TimeValueAscCreatedAtAsc(String date, Long themeId);

    List<Reservation> findByMember_Id(Long memberId);
}
