package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByMemberId(Long memberId);

    boolean existsByMemberAndDateAndTimeAndTheme(Member member, String date, Time time, Theme theme);
}
