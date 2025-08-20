package roomescape.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByMemberId(Long memberId);

    boolean existsByMemberAndDateAndTimeAndTheme(Member member, String date, Time time,
        Theme theme);
}
