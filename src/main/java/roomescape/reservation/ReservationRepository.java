package roomescape.reservation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"member"})
    Optional<Reservation> findWithMemberByDateAndTimeAndTheme(String date, Time time, Theme theme);

    @EntityGraph(attributePaths = {"time", "theme"})
    List<Reservation> findAll();

    @EntityGraph(attributePaths = {"time", "theme"})
    List<Reservation> findByMember(Member member);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
