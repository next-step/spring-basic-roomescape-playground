package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
    List<Reservation> findByMemberId(Long memberId);
    boolean existsByMemberIdAndDateAndTimeAndTheme(Long memberId, String date, Time time, Theme theme);
}
