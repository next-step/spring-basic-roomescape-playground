package roomescape.reservation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.theme.Theme;
import roomescape.time.Time;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time WHERE r.member.id = :memberId")
    List<Reservation> findByMemberIdWithThemeAndTime(@Param("memberId") Long memberId);

    boolean existsByMemberIdAndDateAndTimeAndTheme(Long memberId, String date, Time time,
        Theme theme);

    long countByDateAndTimeAndThemeAndStatus(String date, Time time, Theme theme,
        ReservationStatus reservationStatus);

    Optional<Reservation> findFirstByDateAndTimeAndThemeAndStatus(
        String date, Time time, Theme theme, ReservationStatus status);

    List<Reservation> findByDateAndTimeAndThemeAndStatusOrderByIdAsc(String date, Time time,
        Theme theme, ReservationStatus reservationStatus);
}
