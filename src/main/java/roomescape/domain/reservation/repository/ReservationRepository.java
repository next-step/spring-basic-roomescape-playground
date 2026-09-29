package roomescape.domain.reservation.repository;

import org.springframework.data.repository.ListCrudRepository;
import roomescape.domain.member.entity.Member;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends ListCrudRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    List<Reservation> findAllByMember_Id(Long memberId);

    boolean existsByTheme(Theme theme);

    boolean existsByDateAndTimeAndTheme(LocalDate date, Time time, Theme theme);

    boolean existsByMemberAndDateAndTimeAndTheme(Member member, LocalDate date, Time time, Theme theme);
}
