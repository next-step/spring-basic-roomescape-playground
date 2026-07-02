package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndTheme_IdAndIsWaitingFalse(String date, Long themeId);

    List<Reservation> findByDateAndTheme_IdAndIsWaitingTrueOrderByTime_TimeValueAscIdAsc(String date, Long themeId);

    List<Reservation> findByMember_Id(Long memberId);
}
